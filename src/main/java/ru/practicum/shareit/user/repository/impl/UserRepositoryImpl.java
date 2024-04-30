package ru.practicum.shareit.user.repository.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users;
    private long idCounter;

    public UserRepositoryImpl() {
        users = new ArrayList<>();
        idCounter = 1;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
            users.add(user);
        } else {
            Optional<User> existingUser = findById(user.getId());
            if (existingUser.isPresent()) {
                int index = users.indexOf(existingUser.get());
                users.set(index, user);
            }
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
