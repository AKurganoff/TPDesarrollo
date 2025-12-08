package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Usuario;
import com.Diseno.TPDiseno2025.Model.UsuarioDTO;
import com.Diseno.TPDiseno2025.Util.AuthException;

public interface UsuarioService {
    
    UsuarioDTO crearUsuario(UsuarioDTO usDTO);
    void cambiarPsw(UsuarioDTO usDTO, String newpsw) throws AuthException;
    Usuario validarUsuario(UsuarioDTO usDTO) throws AuthException;
    Usuario mapToUsuarioEntidad(Usuario us, UsuarioDTO usDTO);
    UsuarioDTO mapToUsuarioDTO(Usuario us, UsuarioDTO usDTO);
    List<Usuario> findAll();
}
