package ufrn.imd.br.msauthserver.repository;


import ufrn.imd.br.msauthserver.model.Doctor;

import java.util.Optional;

public interface DoctorRepository extends GenericRepository<Doctor>{
    boolean existsByCrmIgnoreCase(String crm);

    Optional<Doctor> getDoctorById(Long id);

    Optional<Doctor> getDoctorByCrm(String crm);
}
