package me.xstumble.ppcrudbootsecurity.services;

import me.xstumble.ppcrudbootsecurity.models.User;

import java.util.List;

public interface UserService {
    long addUser(User user);
    void updateUser(long id, User user);
    void deleteUser(long id);
    User getUser(long id);
    List<User> getAllUsers();

}
