package com.Diseno.TPDiseno2025.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Diseno.TPDiseno2025.Domain.Usuario;
import com.Diseno.TPDiseno2025.Domain.UsuarioId;
import com.Diseno.TPDiseno2025.Model.UsuarioDTO;
import com.Diseno.TPDiseno2025.Repository.UsuarioRepository;
import com.Diseno.TPDiseno2025.Service.UsuarioServiceImp;
import com.Diseno.TPDiseno2025.Util.AuthException;
import com.Diseno.TPDiseno2025.Util.NotFoundException;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImpTest {

    @InjectMocks
    private UsuarioServiceImp usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioDTO usuarioDTO;
    private UsuarioId usuarioId;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre_usuario("admin");
        usuarioDTO.setPsw("1234");

        usuarioId = new UsuarioId();
        usuarioId.setNombre("admin");
        usuarioId.setPsw("1234");

        usuario = new Usuario();
        usuario.setCredenciales(usuarioId);
    }

    @Test
    void crearUsuario_ok() {
        when(usuarioRepository.existsById(any())).thenReturn(false);

        UsuarioDTO result = usuarioService.crearUsuario(usuarioDTO);

        assertNotNull(result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_usuarioExistente_lanzaAuthException() {
        when(usuarioRepository.existsById(any())).thenReturn(true);

        assertThrows(AuthException.class,
                () -> usuarioService.crearUsuario(usuarioDTO));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void validarUsuario_ok() {
        when(usuarioRepository
                .findByCredenciales_NombreStartingWithIgnoreCase("admin"))
                .thenReturn(List.of(usuario));

        Usuario result = usuarioService.validarUsuario(usuarioDTO);

        assertNotNull(result);
        assertEquals("admin", result.getCredenciales().getNombre());
    }

    @Test
    void validarUsuario_credencialesIncorrectas() {
        when(usuarioRepository
                .findByCredenciales_NombreStartingWithIgnoreCase("admin"))
                .thenReturn(List.of());

        assertThrows(AuthException.class,
                () -> usuarioService.validarUsuario(usuarioDTO));
    }

    @Test
    void cambiarPsw_ok() {
        when(usuarioRepository
                .findByCredenciales_NombreStartingWithIgnoreCase("admin"))
                .thenReturn(List.of(usuario));

        when(usuarioRepository.findById(any()))
                .thenReturn(Optional.of(usuario));

        usuarioService.cambiarPsw(usuarioDTO, "newpass");

        verify(usuarioRepository).delete(usuario);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void cambiarPsw_usuarioNoEncontrado() {
        when(usuarioRepository
                .findByCredenciales_NombreStartingWithIgnoreCase("admin"))
                .thenReturn(List.of(usuario));

        when(usuarioRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> usuarioService.cambiarPsw(usuarioDTO, "newpass"));
    }

    @Test
    void findAll_ok() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> result = usuarioService.findAll();

        assertEquals(1, result.size());
        verify(usuarioRepository).findAll();
    }
}

