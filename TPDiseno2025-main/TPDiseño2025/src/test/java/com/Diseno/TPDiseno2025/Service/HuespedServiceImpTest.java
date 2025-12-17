package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.*;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Repository.DireccionRepository;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;
import com.Diseno.TPDiseno2025.Repository.TelefonoRepository;
import com.Diseno.TPDiseno2025.Util.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HuespedServiceImpTest {

    @Mock private DireccionService direccionService;
    @Mock private DireccionRepository direccionRepository;
    @Mock private HuespedRepository huespedRepository;
    @Mock private TelefonoRepository telefonoRepository;

    @InjectMocks
    private HuespedServiceImp service;

    private HuespedDTO buildDtoEjemplo() {
        HuespedDTO dto = new HuespedDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setTipoDni("DNI");
        dto.setDni(32053123);
        dto.setOcupacion("Empleado");
        dto.setPosIva("ConsumidorFinal");
        dto.setEmail("a@a.com");
        dto.setTelefono("3421234567");

        DireccionDTO dir = new DireccionDTO();
        dir.setCalle("San Martin");
        dir.setNumero(1000);
        dir.setDepartamento("A");
        dir.setPiso(1);
        dir.setCodPostal(3000);
        dir.setLocalidad("Santa Fe");
        dir.setProvincia("Santa Fe");
        dir.setPais("Argentina");

        dto.setDireccion(dir);
        return dto;
    }

    private Direccion buildDireccion(DireccionId id) {
        Direccion d = new Direccion();
        d.setId(id);
        return d;
    }

    // Tests

    @Test
    void buscarHuespedByTipoDniAndDni_cuandoExiste() {
        Huesped h = new Huesped();
        when(huespedRepository.findByTipoDniAndDni("DNI", 32053123)).thenReturn(Optional.of(h));

        Huesped res = service.buscarHuespedByTipoDniAndDni("DNI", 32053123);

        assertNotNull(res);
        verify(huespedRepository).findByTipoDniAndDni("DNI", 32053123);
    }

    @Test
    void buscarHuespedByTipoDniAndDni_cuandoNoExiste() {
        when(huespedRepository.findByTipoDniAndDni("DNI", 32053123)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.buscarHuespedByTipoDniAndDni("DNI", 32053123));
    }

    @Test
    void modificarHuesped_actualizaYGuarda() {
        Huesped existente = new Huesped();
        existente.setNombre("Fran");
        existente.setApellido("Lopez");

        Direccion dir = new Direccion();
        Huesped actualizado = new Huesped();
        actualizado.setNombre("Martin");
        actualizado.setApellido("Gomez");
        actualizado.setDireccion(dir);

        when(huespedRepository.findByTipoDniAndDni("DNI", 32053123)).thenReturn(Optional.of(existente));

        service.modificarHuesped("DNI", 32053123, actualizado);

        assertEquals("Martin", existente.getNombre());
        assertEquals("Gomez", existente.getApellido());
        assertEquals(dir, existente.getDireccion());
        verify(huespedRepository).save(existente);
    }

    @Test
    void validarDatos_dtoNull() {
        assertThrows(IllegalArgumentException.class, () -> service.validarDatos(null));
    }

    @Test
    void validarDatos_nombreVacio() {
        HuespedDTO dto = buildDtoEjemplo();
        dto.setNombre("   ");

        assertThrows(IllegalArgumentException.class, () -> service.validarDatos(dto));
    }

    @Test
    void crearHuespedDTO_cuandoNoVieneTelefono_noGuardaTelefono() {
        HuespedDTO dto = buildDtoEjemplo();
        dto.setTelefono("   "); // vacio

        when(direccionService.direccionExists(anyString(), anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(true);

        DireccionId id = new DireccionId(dto.getDireccion().getCalle(),
                dto.getDireccion().getNumero(),
                dto.getDireccion().getDepartamento(),
                dto.getDireccion().getPiso(),
                dto.getDireccion().getCodPostal());

        Direccion direccion = buildDireccion(id);
        when(direccionRepository.findById(any(DireccionId.class))).thenReturn(Optional.of(direccion));

        when(huespedRepository.existsByTipoDniAndDni(dto.getTipoDni(), dto.getDni())).thenReturn(false);

        Huesped guardado = new Huesped();
        guardado.setDni(dto.getDni());
        when(huespedRepository.save(any(Huesped.class))).thenReturn(guardado);

        Integer dniRes = service.crearHuespedDTO(dto);

        assertEquals(dto.getDni(), dniRes);
        verify(huespedRepository).save(any(Huesped.class));
        verify(telefonoRepository, never()).save(any(Telefono.class));
    }
}