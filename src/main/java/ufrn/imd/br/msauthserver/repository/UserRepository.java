package ufrn.imd.br.msauthserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ufrn.imd.br.msauthserver.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User>{
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);
}
