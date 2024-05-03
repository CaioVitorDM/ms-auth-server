package ufrn.imd.br.msauthserver.mappers;

import org.springframework.stereotype.Component;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.model.Patient;

@Component
public class PatientMapper implements DtoMapper<Patient, PatientDTO> {

    @Override
    public PatientDTO toDto(Patient entity) {
        return new PatientDTO(
                entity.getId(),
                entity.getDoctorId(),
                entity.getCpf(),
                entity.getBirthDate(),
                entity.getName()
        );
    }

    @Override
    public Patient toEntity(PatientDTO patientDTO) {
        return Patient.builder()
                .id(patientDTO.id())
                .doctorId(patientDTO.doctorId())
                .cpf(patientDTO.cpf())
                .birthDate(patientDTO.birthDate())
                .name(patientDTO.name())
                .build();
    }
}
