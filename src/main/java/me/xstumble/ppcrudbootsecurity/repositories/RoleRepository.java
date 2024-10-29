package me.xstumble.ppcrudbootsecurity.repositories;

import me.xstumble.ppcrudbootsecurity.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
