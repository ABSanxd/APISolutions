package com.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse
 * 
 * Estructura estándar JSend para las respuestas de la API.
 * 
 * Ejemplo:
 *  {
 *      "status": "success" | "fail" | "error",
 *      "data": { ... },
 *      "message": "descripción opc",
 *      "code": código HTTP opc
 *  }
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String status; // success | fail | error
    private T data;       
    private String message;
    private Integer code;  

    // Constructor rápido para respuestas exitosas
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data, null, 200);
    }

    // Respuesta con mensaje personalizado
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("success", data, message, 200);
    }

    // Fallo controlado (por validaciones, datos faltantes, etc.)
    public static <T> ApiResponse<T> fail(String message, int code) {
        return new ApiResponse<>("fail", null, message, code);
    }

    // Error inesperado del servidor
    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>("error", null, message, code);
    }
}
