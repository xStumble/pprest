package me.xstumble.ppcrudbootsecurity.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import me.xstumble.ppcrudbootsecurity.exceptions.EntityExistsExceptionWithType;
import me.xstumble.ppcrudbootsecurity.exceptions.UserValidationException;
import me.xstumble.ppcrudbootsecurity.models.User;
import me.xstumble.ppcrudbootsecurity.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminRESTController {

    private final UserService userService;

    public AdminRESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getallusers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getuser")
    public User getUser(@RequestParam long id) {
        return userService.getUser(id);
    }


    @PostMapping("/adduser")
    public ResponseEntity<Long> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult);
        }
        long id = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/edituser")
    public ResponseEntity<HttpStatus> editUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("password")).toList();
        BindingResult sortedBindingResult = new BeanPropertyBindingResult(user, "user");
        fieldErrors.forEach(sortedBindingResult::addError);
        if (sortedBindingResult.hasErrors()) {
            throw new UserValidationException(sortedBindingResult);
        }

        userService.updateUser(user.getId(), user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/deleteuser")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody User user) {
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<Map<String, List<String>>> handleException(UserValidationException e) {
        Map<String, List<String>> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), new ArrayList<>()));
        e.getBindingResult().getFieldErrors().forEach(err -> errors.get(err.getField()).add(err.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(EntityExistsExceptionWithType e) {
        String response = "{\"" + e.getType() + "\": [\"" + e.getMessage() + "\"]}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ResponseEntity<String> handleException(EntityNotFoundException e) {
        String response = e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
