package me.xstumble.ppcrudbootsecurity.controllers;

import me.xstumble.ppcrudbootsecurity.models.User;
import me.xstumble.ppcrudbootsecurity.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRESTController {

    @GetMapping("/getcurrentuser")
    public User getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUser();
    }

}
