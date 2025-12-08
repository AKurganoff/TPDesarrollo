package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Usuario;
import com.Diseno.TPDiseno2025.Domain.UsuarioId;
import com.Diseno.TPDiseno2025.Model.UsuarioDTO;
import com.Diseno.TPDiseno2025.Repository.UsuarioRepository;
import com.Diseno.TPDiseno2025.Util.AuthException;
import com.Diseno.TPDiseno2025.Util.NotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImp implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usDTO){
        UsuarioId usId = new UsuarioId();
        usId.setNombre(usDTO.getNombre_usuario());
        usId.setPsw(usDTO.getPsw());
        if(usuarioRepository.existsById(usId)){
            throw( new AuthException("Ya existe el nombre de usuario en el sistema"));
        }
        Usuario us = new Usuario();
        us.setCredenciales(usId);
        usuarioRepository.save(us);
        return usDTO;
    }

    @Override
    public void cambiarPsw(UsuarioDTO usDTO, String newpsw) throws AuthException{
        UsuarioId usId = new UsuarioId();
        usId.setNombre(usDTO.getNombre_usuario());
        usId.setPsw(usDTO.getPsw());
        validarUsuario(usDTO);
        UsuarioId newUsId = new UsuarioId();
        newUsId.setNombre(usDTO.getNombre_usuario());
        newUsId.setPsw(newpsw);
        Usuario oldUs = usuarioRepository.findById(usId).
                    orElseThrow(()->
                    new NotFoundException("Usuario no registrado"));
        Usuario newUs = new Usuario();
        newUs.setCredenciales(newUsId);
        usuarioRepository.delete(oldUs);
        usuarioRepository.save(newUs);
    }

    @Override
    public Usuario validarUsuario(UsuarioDTO usDTO) throws AuthException{
        List<Usuario> users = usuarioRepository.findByCredenciales_NombreStartingWithIgnoreCase(usDTO.getNombre_usuario());
        if(users != null){
            for(Usuario u : users){
                if((u.getCredenciales().getPsw().compareTo(usDTO.getPsw())==0)){
                    return this.mapToUsuarioEntidad(new Usuario(), usDTO);
                }
            }
        }
        throw( new AuthException("Credenciales incorrectas"));
    }

    @Override
    public Usuario mapToUsuarioEntidad(Usuario us, UsuarioDTO usDTO){
        UsuarioId usId = new UsuarioId();
        usId.setNombre(usDTO.getNombre_usuario());
        usId.setPsw(usDTO.getPsw());
        us.setCredenciales(usId);
        return us;
    }

    @Override
    public UsuarioDTO mapToUsuarioDTO(Usuario us, UsuarioDTO usDTO){
        usDTO.setNombre_usuario(us.getCredenciales().getNombre());
        usDTO.setPsw(us.getCredenciales().getPsw());
        return usDTO;
    }

    @Override
    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }
}
