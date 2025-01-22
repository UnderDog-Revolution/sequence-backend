//package sequence.sequence_member.deleteAccount.repository;
//
//import sequence.sequence_member.deleteAccount.entity.DeletedUserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface DeletedUserRepository extends JpaRepository<DeletedUserEntity, Long> {
//    Optional<DeletedUserEntity> findByUsername(String username);
//
//    boolean existsByUsername(String username);
//
//    boolean existsByUserId(Long userId);
//}
