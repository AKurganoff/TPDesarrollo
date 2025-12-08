package com.Diseno.TPDiseno2025.Model;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HuespedDTO {
    
    @NotBlank
    @Size(max = 50)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    private String apellido;

    @NotBlank
    @Size(max = 20)
    private String tipoDni;

    @NotNull
    private Integer dni;

    @NotNull
    private DireccionDTO direccion;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    private LocalDate fechaNacimiento;

    
    private Integer edad;

    @NotBlank
    @Size(max = 50)
    private String ocupacion;

    @NotBlank
    @Size(max = 50)
    private String posIva;

    private String nacionalidad;

    @NotNull
    private String telefono;

}
