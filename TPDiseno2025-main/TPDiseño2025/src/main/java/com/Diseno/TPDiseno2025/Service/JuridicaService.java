package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.Juridica;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Model.JuridicaDTO;

public interface JuridicaService {
    
    void crearJuridica(Integer cuit, String razonSocial, Direccion dir);
    JuridicaDTO crearJuridicaDTO(Integer cuit, String razonSocial, DireccionDTO dir);
    Juridica findByCuit(Integer cuit);
    Integer getCuit(Juridica j);
    boolean existsByCuit(String cuit);
    List<Juridica> obtenerTodas();
    Juridica mapToEntJuridica(JuridicaDTO jdTO, Juridica j);
    JuridicaDTO mapToDTOJuridica(Juridica j, JuridicaDTO jdTO);
}
