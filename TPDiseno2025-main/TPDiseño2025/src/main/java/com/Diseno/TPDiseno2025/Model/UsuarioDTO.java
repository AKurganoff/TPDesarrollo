package com.Diseno.TPDiseno2025.Model;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    
    @NotNull
    @Size(max = 20)
    private String nombre_usuario;

    @NotNull
    @Size(max = 20)
    private String psw;

}
