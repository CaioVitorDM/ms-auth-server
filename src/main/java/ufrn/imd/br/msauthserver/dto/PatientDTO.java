package ufrn.imd.br.msauthserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PatientDTO(
        long id,
        long doctorId,
        String cpf,
        LocalDate birthDate,
        String name
) implements EntityDTO{

    @Override
    public PatientDTO toResponse(){
        return new PatientDTO(
                this.id(),
                this.doctorId(),
                this.cpf(),
                this.birthDate(),
                this.name()
        );
    }
}