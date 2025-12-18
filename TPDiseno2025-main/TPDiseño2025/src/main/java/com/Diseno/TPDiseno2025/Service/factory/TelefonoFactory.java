package com.Diseno.TPDiseno2025.Service.factory;

import org.springframework.stereotype.Component;
import com.Diseno.TPDiseno2025.Domain.Telefono;
import com.Diseno.TPDiseno2025.Domain.Huesped;
@Component
public class TelefonoFactory {

    public Telefono crear(Huesped huesped, String telefono) {

        Telefono tel = new Telefono();
        tel.setHuesped(huesped);
        tel.setTelefono(telefono);
        return tel;
    }
}
