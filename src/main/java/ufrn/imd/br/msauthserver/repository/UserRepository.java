package ufrn.imd.br.msauthserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User>, CustomUserRepository{
    Optional<User> findByLoginIgnoreCase(String login);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByPhoneNumberIgnoreCase(String phoneNumber);

    @Query("select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);

    Optional<User> findByDoctorId(long doctorId);



}
