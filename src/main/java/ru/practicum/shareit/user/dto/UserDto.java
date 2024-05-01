package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.common.validation.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    @NotBlank(message = "Email must not be blank", groups = Marker.OnCreate.class)
    @Email(message = "Invalid email", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String email;
}
