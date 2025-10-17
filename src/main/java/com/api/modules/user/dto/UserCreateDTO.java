package com.api.modules.user.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del correo no es válido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "Debe seleccionar un departamento")
    private String department;

    @NotBlank(message = "Debe seleccionar una provincia")
    private String province;

    @NotBlank(message = "Debe seleccionar un distrito")
    private String district;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate birthDate;
}
