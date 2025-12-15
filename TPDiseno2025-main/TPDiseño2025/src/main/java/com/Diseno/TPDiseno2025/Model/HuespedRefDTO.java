package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HuespedRefDTO {

    @NotBlank
    @Size(max = 20)
    private String tipoDni;

    @NotNull
    @Positive
    private Integer dni;
}
