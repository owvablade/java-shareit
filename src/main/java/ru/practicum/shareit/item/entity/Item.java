package ru.practicum.shareit.item.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;
}
