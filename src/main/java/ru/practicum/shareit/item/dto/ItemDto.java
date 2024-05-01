package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.common.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "Name must not be blank", groups = Marker.OnCreate.class)
    private String name;

    @NotBlank(message = "Description must not be blank", groups = Marker.OnCreate.class)
    private String description;

    @NotNull(message = "Available must not be null", groups = Marker.OnCreate.class)
    private Boolean available;
}
