package ufrn.imd.br.msauthserver.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.DoctorDTO;
import ufrn.imd.br.msauthserver.mappers.DoctorMapper;
import ufrn.imd.br.msauthserver.mappers.DtoMapper;
import ufrn.imd.br.msauthserver.model.Doctor;
import ufrn.imd.br.msauthserver.repository.DoctorRepository;
import ufrn.imd.br.msauthserver.repository.GenericRepository;
import ufrn.imd.br.msauthserver.utils.exception.BusinessException;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DoctorService implements GenericService<Doctor, DoctorDTO>{

    private final DoctorRepository doctorRepository;
    private final DoctorMapper mapper;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper mapper) {
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Doctor> getRepository() {
        return this.doctorRepository;
    }

    @Override
    public DtoMapper<Doctor, DoctorDTO> getDtoMapper() {
        return this.mapper;
    }

    public DoctorDTO createDoctor(DoctorDTO dto){
        Doctor entity = getDtoMapper().toEntity(dto);

        validateBeforeSave(entity);
        return getDtoMapper().toDto(getRepository().save(entity));
    }

    public DoctorDTO updateDoctor(DoctorDTO dto){
        Doctor updatedEntity = mapper.toEntity(dto);
        Long id = dto.id();

        Doctor bdEntity = doctorRepository.findById(id).orElseThrow(() -> new BusinessException(
                "Error: Medic not found with id [" + id + "]", HttpStatus.NOT_FOUND
        ));

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        validateBeforeSave(bdEntity);

        return mapper.toDto(doctorRepository.save(bdEntity));
    }

    @Override
    public void validateBeforeSave(Doctor entity){
        GenericService.super.validateBeforeSave(entity);
        validateCRM(entity.getCrm(), entity.getId());
    }

    private void validateCRM(String crm, Long id){
        if(crm != null){
            Optional<Doctor> doctor = doctorRepository.findByCrmIgnoreCase(crm);
            if(doctor.isPresent() && (id == null || doctor.get().getId() != id)){
                throw new BusinessException(
                        "CRM inválido: " + crm + ". Já existe um médico cadastrado com esse CRM.",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else{
            throw new BusinessException("Error: O CRM não pode ser nulo.", HttpStatus.BAD_REQUEST);
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
