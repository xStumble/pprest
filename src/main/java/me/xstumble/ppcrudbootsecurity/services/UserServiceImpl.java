package me.xstumble.ppcrudbootsecurity.services;

import jakarta.persistence.EntityNotFoundException;
import me.xstumble.ppcrudbootsecurity.exceptions.EntityExistsExceptionWithType;
import me.xstumble.ppcrudbootsecurity.models.User;
import me.xstumble.ppcrudbootsecurity.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        saveIfNotUsed(user);
    }

    @Transactional
    @Override
    public long addUserWithID(User user) {
        return saveIfNotUsedWithID(user);
    }

    @Transactional
    @Override
    public void updateUser(long id, User user) {
        if (userRepository.existsById(id)) {
            saveIfNotUsed(user);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Transactional
    @Override
    public void deleteUser(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    private void saveIfNotUsed(User user) {
        if (checkIfUserAvailable(user)) {
            userRepository.save(user);
        }
    }

    private long saveIfNotUsedWithID(User user) {
        if (checkIfUserAvailable(user)) {
            userRepository.save(user);
            return user.getId();
        }
        return 0;
    }

    private boolean checkIfUserAvailable(User user) {
        Optional<User> userCheckUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userCheckEmail = userRepository.findByEmail(user.getEmail());

        if (userCheckUsername.isPresent()) {
            if (!userCheckUsername.get().getId().equals(user.getId())) {
                throw new EntityExistsExceptionWithType("Username is already taken!", "username");
            }
        }

        if (userCheckEmail.isPresent()) {
            if (!userCheckEmail.get().getId().equals(user.getId())) {
                throw new EntityExistsExceptionWithType("Email is already taken!", "email");
            }
        }

        return true;
    }
}
