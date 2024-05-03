package ufrn.imd.br.msauthserver.dto;

import java.time.LocalDate;

public record PatientDTO(
        long id,
        long doctorId,
        String cpf,
        LocalDate birthDate,
        String name
) {
}
