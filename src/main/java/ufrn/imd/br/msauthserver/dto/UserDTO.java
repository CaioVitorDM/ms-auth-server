package ufrn.imd.br.msauthserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ufrn.imd.br.msauthserver.model.enums.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        long id,
        PatientDTO patient,
        DoctorDTO doctor,
        String login,
        String password,
        Role role,
        String email,
        String phoneNumber,
        Long imgId
) implements EntityDTO{

    @Override
    public UserDTO toResponse(){
        return new UserDTO(
                this.id(),
                this.patient(),
                this.doctor(),
                this.login(),
                null,
                this.role(),
                this.email(),
                this.phoneNumber(),
                this.imgId()
        );
    }
}
