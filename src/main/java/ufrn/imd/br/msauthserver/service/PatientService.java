package ufrn.imd.br.msauthserver.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.mappers.DtoMapper;
import ufrn.imd.br.msauthserver.mappers.PatientMapper;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.repository.GenericRepository;
import ufrn.imd.br.msauthserver.repository.PatientRepository;
import ufrn.imd.br.msauthserver.utils.exception.BusinessException;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PatientService implements GenericService<Patient, PatientDTO>{

    private final PatientRepository patientRepository;
    private final PatientMapper mapper;


    public PatientService(PatientRepository patientRepository, PatientMapper mapper) {
        this.patientRepository = patientRepository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Patient> getRepository() {
        return this.patientRepository;
    }

    @Override
    public DtoMapper<Patient, PatientDTO> getDtoMapper() {
        return this.mapper;
    }

//    public List<PatientDTO> findAllByDoctorId(Long doctorId) {
//        List<Patient> patients = patientRepository.findAllByDoctorId(doctorId);
//        return getDtoMapper().toDto(patients);
//    }

    public PatientDTO createPatient(PatientDTO dto){
        Patient entity = getDtoMapper().toEntity(dto);

        validateBeforeSave(entity);
        return getDtoMapper().toDto(getRepository().save(entity));
    }

    public PatientDTO updatePatient(PatientDTO dto){
        Patient updatedEntity = mapper.toEntity(dto);
        Long patientId = dto.id();

        Patient bdEntity = patientRepository.findById(patientId).orElseThrow(() -> new BusinessException(
                "Error: Patient not found with id [" + patientId + "]", HttpStatus.NOT_FOUND
        ));

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        validateBeforeSave(bdEntity);

        return mapper.toDto(patientRepository.save(bdEntity));
    }

    @Override
    public void validateBeforeSave(Patient entity){
        GenericService.super.validateBeforeSave(entity);
        validateCpf(entity.getCpf(), entity.getId());
    }

    private void validateCpf(String cpf, Long id){
        if(cpf != null){
            Optional<Patient> patient = patientRepository.findByCpfIgnoreCase(cpf);
            if(patient.isPresent() && (id == null || patient.get().getId() != id)){
                throw new BusinessException(
                        "CPF inválido: " + cpf + ". Já existe um paciente cadastrado com esse cpf.",
                        HttpStatus.BAD_REQUEST);
            }
        }
        else{
            throw new BusinessException("Error: O CPF não pode ser nulo.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves the names of properties with null values from the given object.
     *
     * @param source The object from which to extract property names.
     * @return An array of property names with null values.
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
