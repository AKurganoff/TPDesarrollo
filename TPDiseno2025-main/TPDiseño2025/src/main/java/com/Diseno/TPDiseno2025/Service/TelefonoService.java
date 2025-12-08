package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Model.TelefonoDTO;

public interface TelefonoService {
    
    void crearTelefono(String tel, Huesped h);

    TelefonoDTO crearTelefonoDTO(String tel, Integer idH);

}
