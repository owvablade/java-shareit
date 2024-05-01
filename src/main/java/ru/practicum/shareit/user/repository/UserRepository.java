package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User update(User user);

    Optional<User> findById(Long id);

    void deleteById(Long id);

    List<User> findAll();

    boolean existsById(Long id);
}
