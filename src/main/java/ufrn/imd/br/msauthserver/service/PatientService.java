package ufrn.imd.br.msauthserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.mappers.DtoMapper;
import ufrn.imd.br.msauthserver.mappers.PatientMapper;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.repository.GenericRepository;
import ufrn.imd.br.msauthserver.repository.PatientRepository;
import ufrn.imd.br.msauthserver.utils.exception.BusinessException;

import java.util.List;

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

    @Override
    public void validateBeforeSave(Patient entity){
        GenericService.super.validateBeforeSave(entity);
        validateCpf(entity.getCpf());
    }

    private void validateCpf(String cpf){
        if(patientRepository.existsByCpfIgnoreCase(cpf)){
            throw new BusinessException(
              "CPF inválido: " + cpf + ". Já existe um paciente cadastrado com esse usuário",
              HttpStatus.BAD_REQUEST
            );
        }
    }
}
