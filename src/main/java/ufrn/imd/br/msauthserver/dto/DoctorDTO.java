package ufrn.imd.br.msauthserver.dto;

import java.time.LocalDate;

public record DoctorDTO(
        long id,
        String crm,
        String name,
        LocalDate birthDate
) {
}
