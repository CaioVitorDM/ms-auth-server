package ufrn.imd.br.msauthserver.mappers;

import org.springframework.stereotype.Component;
import ufrn.imd.br.msauthserver.dto.DoctorDTO;
import ufrn.imd.br.msauthserver.model.Doctor;

@Component
public class DoctorMapper implements DtoMapper<Doctor, DoctorDTO> {
    @Override
    public DoctorDTO toDto(Doctor entity) {
        return new DoctorDTO(
                entity.getId(),
                entity.getCrm(),
                entity.getName(),
                entity.getBirthDate()
        );
    }

    @Override
    public Doctor toEntity(DoctorDTO doctorDTO) {
        return Doctor.builder()
                .id(doctorDTO.id())
                .crm(doctorDTO.crm())
                .name(doctorDTO.name())
                .birthDate(doctorDTO.birthDate())
                .build();
    }
}
