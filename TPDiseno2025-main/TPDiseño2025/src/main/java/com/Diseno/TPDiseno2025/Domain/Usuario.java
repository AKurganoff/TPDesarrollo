package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {
    @EmbeddedId
    private UsuarioId credenciales;
}
