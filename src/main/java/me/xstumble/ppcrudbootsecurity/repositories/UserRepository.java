package me.xstumble.ppcrudbootsecurity.repositories;

import me.xstumble.ppcrudbootsecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u left join fetch u.roles where u.username=:username")
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
