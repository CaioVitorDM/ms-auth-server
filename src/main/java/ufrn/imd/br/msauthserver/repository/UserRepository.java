package ufrn.imd.br.msauthserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ufrn.imd.br.msauthserver.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User>{
    boolean existsByLoginIgnoreCase(String login);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    @Query("select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);

    Optional<User> findByDoctorId(long doctorId);
}
