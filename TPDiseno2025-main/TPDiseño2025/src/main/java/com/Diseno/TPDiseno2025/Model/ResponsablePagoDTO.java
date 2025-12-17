package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResponsablePagoDTO {

    @NotNull
    private Integer idResponsablePago;
    
    @NotNull
    private String razonSocial;

    @NotNull
    private String condicionIVA;

    public ResponsablePagoDTO (Integer idResp, String razonSoc, String condIva) {
        this.idResponsablePago = idResp;
        this.razonSocial = razonSoc;
        this.condicionIVA = condIva;
    }
}
