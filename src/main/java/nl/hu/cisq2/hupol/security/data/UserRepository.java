package nl.hu.cisq2.hupol.security.data;

import nl.hu.cisq2.hupol.security.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
}
