package ru.practicum.shareit.item.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Item {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;
}
