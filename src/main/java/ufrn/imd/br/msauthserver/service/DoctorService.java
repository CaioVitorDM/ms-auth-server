package ufrn.imd.br.msauthserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.DoctorDTO;
import ufrn.imd.br.msauthserver.mappers.DoctorMapper;
import ufrn.imd.br.msauthserver.mappers.DtoMapper;
import ufrn.imd.br.msauthserver.model.Doctor;
import ufrn.imd.br.msauthserver.repository.DoctorRepository;
import ufrn.imd.br.msauthserver.repository.GenericRepository;
import ufrn.imd.br.msauthserver.utils.exception.BusinessException;

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

    @Override
    public void validateBeforeSave(Doctor entity){
        GenericService.super.validateBeforeSave(entity);
        validateCRM(entity.getCrm());
    }

    private void validateCRM(String crm){
        if(doctorRepository.existsByCrmIgnoreCase(crm)){
            throw new BusinessException(
                    "CRM inválido: " + crm + ". Já existe um médico cadastrado com esse CRM.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
