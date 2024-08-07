package ufrn.imd.br.msauthserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User>, CustomUserRepository{
    Optional<User> findByLoginIgnoreCase(String login);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByPhoneNumberIgnoreCase(String phoneNumber);

    @Query("select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);

    Optional<User> findByDoctorId(long doctorId);

    @Query("SELECT u FROM User u JOIN Patient p ON u.patientId = p.id WHERE p.doctorId = :doctorId AND FUNCTION('DATE', u.createdAt) >= :fromDate")
    List<User> findRecentPatientsByDoctor(@Param("doctorId") String doctorId, @Param("fromDate") LocalDate fromDate, Pageable pageable);



}
