package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public Optional<User> create(User user) {
        return this.users.create(user);
    }

    public User findById(int id) {
        return this.users.findById(id);
    }

    public User findUserByEmailAndPassword(String email, String password) {
        return this.users.findUserByEmailAndPassword(email, password);
    }
}
