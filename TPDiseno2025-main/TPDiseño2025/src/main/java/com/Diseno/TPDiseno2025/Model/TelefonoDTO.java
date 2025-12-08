package com.Diseno.TPDiseno2025.Model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TelefonoDTO {

    
    @NotNull
    private String telefono;
    
    private Integer huesped;
}
