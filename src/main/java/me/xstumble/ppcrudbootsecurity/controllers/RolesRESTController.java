package me.xstumble.ppcrudbootsecurity.controllers;

import me.xstumble.ppcrudbootsecurity.models.Role;
import me.xstumble.ppcrudbootsecurity.services.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
public class RolesRESTController {

    private final RoleService roleService;

    public RolesRESTController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }

}
