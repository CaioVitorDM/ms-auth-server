package ufrn.imd.br.msauthserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User>, CustomUserRepository{
    boolean existsByLoginIgnoreCase(String login);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    @Query("select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);

    Optional<User> findByDoctorId(long doctorId);

    @Query("SELECT u FROM User u LEFT JOIN Patient p ON p.id = u.patientId AND p.active = TRUE WHERE p.doctorId = :doctorId")
    List<User> findByPatientDoctorId(@Param("doctorId") Long doctorId);
}
