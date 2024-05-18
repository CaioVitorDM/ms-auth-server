package ufrn.imd.br.msauthserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ufrn.imd.br.msauthserver.model.User;

public interface CustomUserRepository {
    Page<User> searchByFilters(String name, String email, String phoneNumber, String doctorId, Pageable pageable);
}
