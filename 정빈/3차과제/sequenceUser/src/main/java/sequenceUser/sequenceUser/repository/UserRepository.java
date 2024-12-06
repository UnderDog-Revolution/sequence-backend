package sequenceUser.sequenceUser.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sequenceUser.sequenceUser.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
}

