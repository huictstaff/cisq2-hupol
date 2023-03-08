package nl.hu.cisq2.hupol.security.data;

import nl.hu.cisq2.hupol.security.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}
