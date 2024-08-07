package ufrn.imd.br.msauthserver.repository;


import ufrn.imd.br.msauthserver.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends GenericRepository<Patient>{
    boolean existsByCpfIgnoreCase(String cpf);
    Optional<Patient> getPatientById(Long id);
    Optional<Patient> findByCpfIgnoreCase(String cpf);

    boolean existsById(Long id);

}
