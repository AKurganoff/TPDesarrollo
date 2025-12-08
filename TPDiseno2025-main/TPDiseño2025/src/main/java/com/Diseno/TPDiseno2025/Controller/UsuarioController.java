package com.Diseno.TPDiseno2025.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Diseno.TPDiseno2025.Domain.Usuario;
import com.Diseno.TPDiseno2025.Model.UsuarioDTO;
import com.Diseno.TPDiseno2025.Service.UsuarioService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class UsuarioController {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    @GetMapping("/debug/usuarios")
    public ResponseEntity<List<Usuario>> verTodosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    public UsuarioController(final UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody UsuarioDTO usDTO) {
        // Llamamos al servicio para que guarde al usuario
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usDTO);
        Usuario usuarioRegistrado = usuarioService.mapToUsuarioEntidad(new Usuario(), nuevoUsuario);
        // Devolvemos código 201 (CREATED) y el usuario creado
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistrado);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioDTO usDTO) {
        // Llamamos al servicio. Si falla, el servicio lanzará tu AuthException
        Usuario usuarioLogueado = usuarioService.validarUsuario(usDTO); 
        return ResponseEntity.ok(usuarioLogueado);
    }
}
