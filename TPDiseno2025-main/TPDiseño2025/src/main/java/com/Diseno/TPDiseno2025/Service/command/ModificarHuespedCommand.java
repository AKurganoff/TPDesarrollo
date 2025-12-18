package com.Diseno.TPDiseno2025.Service.command;

import com.Diseno.TPDiseno2025.Model.DireccionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModificarHuespedCommand {
    private Integer dni;
    private String tipoDni;
    private String nombre;
    private String apellido;
    private String email;
    private String ocupacion;
    private String posIva;
    private DireccionDTO direccion;
    private String telefono;
}
