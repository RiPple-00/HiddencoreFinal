package hiddencore.ddasum.backend.repository;


import hiddencore.ddasum.backend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository 

public interface MemberRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(Long userId);

    Optional<Users> findByLoginId(String loginId);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhone(String phone);

    boolean existsByLoginId(String loginId);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
