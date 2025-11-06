package com.api.modules.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection; // <--- AÑADIR IMPORT
import java.util.List; // <--- AÑADIR IMPORT
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority; // <--- AÑADIR IMPORT
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <--- AÑADIR IMPORT
import org.springframework.security.core.userdetails.UserDetails; // <--- AÑADIR IMPORT

import com.api.common.enums.Status;
import com.api.common.enums.UserLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data // Parte de la Data
@Entity // Es una entidad
@Table(name = "users")
// --- AÑADIR "implements UserDetails" ---
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt

    @Column(nullable = false)
    private int maxPets = 2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLevel userLevel = UserLevel.BRONCE;

    @Column(nullable = false)
    private int userXp = 0;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVO;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    // --- INICIO DE MÉTODOS UserDetails ---
    // Añade todos estos métodos al final de tu clase

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Como no usas roles (ej. ROLE_ADMIN, ROLE_USER), 
        // le damos una autoridad simple para que Spring esté contento.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        // Spring Security usará el email como "username"
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Asumimos que las cuentas no expiran
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != Status.PAUSADO; // O el estado que uses para bloquear
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Asumimos que las credenciales no expiran
    }

    @Override
    public boolean isEnabled() {
        return this.status == Status.ACTIVO; // La cuenta está activa
    }
    // --- FIN DE MÉTODOS UserDetails ---
}