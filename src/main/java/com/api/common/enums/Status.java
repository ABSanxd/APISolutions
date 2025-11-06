package com.api.common.enums;

// Se agregaran mas de ser necesarios
public enum Status {
    // Estados existentes
    PENDIENTE, // Usado para Publicaciones (esperando aprobación) Y para Solicitudes (esperando respuesta)
    ADOPTADO,  // Usado para Publicaciones (Mascota ya adoptada)
    ACTIVO,    // Usado para Ads, Users, Pets, Publicaciones
    INACTIVO,  // Usado para Ads, Pets
    ELIMINADO, // Usado para Publicaciones
    EXPIRADO,  // Usado para Ads
    PAUSADO,   // Usado para Publicaciones
    // --- NUEVOS ESTADOS AÑADIDOS ---
    ACEPTADO,  
    RECHAZADO,
    CANCELADO  
}