package me.xstumble.ppcrudbootsecurity.util;

import me.xstumble.ppcrudbootsecurity.models.User;
import me.xstumble.ppcrudbootsecurity.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            if (!userRepository.findByUsername(user.getUsername()).get().getId().equals(user.getId())) {
                errors.rejectValue("username", "", "Username is already taken!");
            }
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            if (!userRepository.findByEmail(user.getEmail()).get().getId().equals(user.getId())) {
                errors.rejectValue("email", "", "Email is already taken!");
            }
        }
    }
}
