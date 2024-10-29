package me.xstumble.ppcrudbootsecurity.services;

import me.xstumble.ppcrudbootsecurity.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    public List<Role> getAllRoles();
}
