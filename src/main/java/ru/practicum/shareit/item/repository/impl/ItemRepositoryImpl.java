package ru.practicum.shareit.item.repository.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, List<Item>> items;
    private long idCounter;

    public ItemRepositoryImpl() {
        items = new HashMap<>();
        idCounter = 1;
    }

    @Override
    public Item save(Item item) {
        List<Item> userItems = items.computeIfAbsent(item.getOwner(), k -> new ArrayList<>());
        item.setId(idCounter++);
        userItems.add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        List<Item> userItems = items.get(item.getOwner());
        Optional<Item> existingItem = findById(item.getId());
        if (existingItem.isPresent()) {
            int index = userItems.indexOf(existingItem.get());
            userItems.set(index, item);
        }
        return item;
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        Optional<Item> item = Optional.empty();
        for (List<Item> userItems : items.values()) {
            item = userItems.stream().filter(i -> i.getId().equals(itemId)).findFirst();
        }
        return item;
    }

    @Override
    public List<Item> findAllByUser(Long userId) {
        List<Item> userItems = items.get(userId);
        if (userItems == null) {
            return Collections.emptyList();
        }
        return userItems;
    }

    @Override
    public List<Item> searchItems(String query) {
        List<Item> result = new ArrayList<>();
        for (List<Item> userItems : items.values()) {
            result.addAll(userItems.stream()
                    .filter(Item::getAvailable)
                    .filter(i -> i.getName().toLowerCase().contains(query.toLowerCase())
                            || i.getDescription().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        return result;
    }
}
