package com.Diseno.TPDiseno2025.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Telefono;
import com.Diseno.TPDiseno2025.Model.TelefonoDTO;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;
import com.Diseno.TPDiseno2025.Repository.TelefonoRepository;

@Service
public class TelefonoServiceImp implements TelefonoService{
    
    @Autowired
    private TelefonoRepository telefonoRepository;

    @Autowired 
    private HuespedRepository huespedRepository;

    public void crearTelefono(String tel, Huesped h){
        Telefono telefono = new Telefono();
        telefono.setHuesped(h);
        telefono.setTelefono(tel);
        telefonoRepository.save(telefono);
    }

    public TelefonoDTO crearTelefonoDTO(String tel, Integer idH){
        TelefonoDTO telDTO = new TelefonoDTO();
        telDTO.setHuesped(idH);
        telDTO.setTelefono(tel);
        return telDTO;
    }

}
