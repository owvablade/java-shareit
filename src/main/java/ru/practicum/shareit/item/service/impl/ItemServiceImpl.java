package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerNotFoundException;
import ru.practicum.shareit.item.exception.ItemUpdateOwnerException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        checkUserExists(userId);
        Item item = ItemMapper.toItemEntity(itemDto);
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        checkUserExists(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException(String.format("Item with ID = %d not found", itemDto.getId())));

        if (!Objects.equals(item.getOwner(), userId)) {
            throw new ItemUpdateOwnerException(
                    String.format("Item owner ID cannot be updated from %d to %d", item.getOwner(), userId));
        }

        item.setName(itemDto.getName() == null ? item.getName() : itemDto.getName());
        item.setDescription(itemDto.getDescription() == null ? item.getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? item.getAvailable() : itemDto.getAvailable());

        return ItemMapper.toItemDto(itemRepository.update(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Item with ID = %d not found", itemId)));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        checkUserExists(userId);
        return itemRepository.findAllByUser(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String query) {
        if (query.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(query).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ItemOwnerNotFoundException(String.format("Owner (user) with ID = %d not found", userId));
        }
    }
}
