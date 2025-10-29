package com.api.modules.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    private String department;
    private String province;
    private String district;
}
