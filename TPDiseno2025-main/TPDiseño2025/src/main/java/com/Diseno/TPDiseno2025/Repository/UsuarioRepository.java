package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Usuario;
import com.Diseno.TPDiseno2025.Domain.UsuarioId;


@Repository
public interface  UsuarioRepository  extends JpaRepository<Usuario, UsuarioId> {
    
    @Override
    Optional<Usuario> findById(UsuarioId credenciales);

    List<Usuario> findByCredenciales_NombreStartingWithIgnoreCase(String nombre);
    
}
