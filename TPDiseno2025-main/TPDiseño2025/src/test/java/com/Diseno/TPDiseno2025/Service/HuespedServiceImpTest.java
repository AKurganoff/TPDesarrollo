package com.Diseno.TPDiseno2025.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;  
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Telefono;
import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;

import com.Diseno.TPDiseno2025.Repository.HuespedRepository;
import com.Diseno.TPDiseno2025.Repository.DireccionRepository;
import com.Diseno.TPDiseno2025.Repository.TelefonoRepository;

import com.Diseno.TPDiseno2025.Service.HuespedServiceImp;
import com.Diseno.TPDiseno2025.Service.DireccionService;

import com.Diseno.TPDiseno2025.Service.strategy.BajaHuespedContext;
import com.Diseno.TPDiseno2025.Service.strategy.BajaModo;

import com.Diseno.TPDiseno2025.Util.NotFoundException;

@ExtendWith(MockitoExtension.class)
class HuespedServiceImpTest {

    @InjectMocks
    private HuespedServiceImp huespedService;

    @Mock
    private HuespedRepository huespedRepository;

    @Mock
    private DireccionService direccionService;

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private TelefonoRepository telefonoRepository;

    @Mock
    private BajaHuespedContext bajaHuespedContext;

    @Test
    void buscarHuesped_existente_devuelveHuesped() {
        Huesped h = new Huesped();
        h.setDni(123);

        when(huespedRepository.findByTipoDniAndDni("DNI", 123))
            .thenReturn(Optional.of(h));

        Huesped resultado =
            huespedService.buscarHuespedByTipoDniAndDni("DNI", 123);

        assertNotNull(resultado);
        assertEquals(123, resultado.getDni());
    }

    @Test
    void buscarHuesped_inexistente_lanzaNotFound() {
        when(huespedRepository.findByTipoDniAndDni("DNI", 999))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            huespedService.buscarHuespedByTipoDniAndDni("DNI", 999));
    }

    @Test
    void buscarHuespedDTO_existente_devuelveDTO() {
        Huesped h = new Huesped();
        h.setDni(123);
        h.setNombre("Juan");

        Direccion dir = new Direccion();
        h.setDireccion(dir);

        when(huespedRepository.findByTipoDniAndDni("DNI", 123))
            .thenReturn(Optional.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
            .thenReturn(new DireccionDTO());

        HuespedDTO dto =
            huespedService.buscarHuespedDTOByTipoDniAndDni("DNI", 123);

        assertNotNull(dto);
        assertEquals(123, dto.getDni());
    }

    @Test
    void obtenerTodosDTO_devuelveLista() {
        Huesped h = new Huesped();
        h.setDni(1);
        h.setDireccion(new Direccion());

        when(huespedRepository.findAll())
            .thenReturn(List.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
            .thenReturn(new DireccionDTO());

        List<HuespedDTO> resultado = huespedService.obtenerTodosDTO();

        assertEquals(1, resultado.size());
    }
    // -------------------- darDeBajaHuesped --------------------
    @Test
    void darDeBajaHuesped_fisica_eliminaTelefonos() {
        huespedService.darDeBajaHuesped("DNI", 123, "FISICA");

        verify(telefonoRepository)
            .deleteByHuesped_Dni(123);

        verify(bajaHuespedContext)
            .darDeBaja("DNI", 123, BajaModo.FISICA);
    }

    @Test
    void darDeBajaHuesped_logica_noEliminaTelefonos() {
        huespedService.darDeBajaHuesped("DNI", 123, "LOGICA");

        verify(telefonoRepository, never())
            .deleteByHuesped_Dni(any());

        verify(bajaHuespedContext)
            .darDeBaja("DNI", 123, BajaModo.LOGICA);
    }

    @Test
    void crearHuespedDTO_direccionNoExiste_huespedNuevo_conTelefono() {
        DireccionDTO dirDTO = new DireccionDTO();
        dirDTO.setCalle("Calle");
        dirDTO.setNumero(123);
        dirDTO.setDepartamento("-");
        dirDTO.setPiso(0);
        dirDTO.setCodPostal(1000);

        HuespedDTO dto = new HuespedDTO();
        dto.setDni(12345678);
        dto.setTipoDni("DNI");
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setDireccion(dirDTO);
        dto.setTelefono("123456");

        DireccionId dirId = new DireccionId("Calle", 123, "-", 0, 1000);
        Direccion direccion = new Direccion();
        direccion.setId(dirId);

        when(direccionService.direccionExists(any(), any(), any(), any(), any()))
                .thenReturn(false);
        when(direccionService.crearDireccionId(any(), any(), any(), any(), any()))
                .thenReturn(dirId);
        when(direccionRepository.findById(dirId))
                .thenReturn(Optional.of(direccion));

        when(huespedRepository.existsByTipoDniAndDni(any(), any()))
                .thenReturn(false);
        when(huespedRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Integer dni = huespedService.crearHuespedDTO(dto);

        // Assert
        assertEquals(12345678, dni);
        verify(huespedRepository).save(any(Huesped.class));
        verify(telefonoRepository).save(any(Telefono.class));
    }

    @Test
    void crearHuespedDTO_direccionExiste_huespedExistente_sinTelefono() {
        DireccionDTO dirDTO = new DireccionDTO();
        dirDTO.setCalle("Calle");
        dirDTO.setNumero(123);
        dirDTO.setDepartamento("-");
        dirDTO.setPiso(0);
        dirDTO.setCodPostal(1000);

        HuespedDTO dto = new HuespedDTO();
        dto.setDni(111);
        dto.setTipoDni("DNI");
        dto.setDireccion(dirDTO);

        DireccionId dirId = new DireccionId("Calle", 123, "-", 0, 1000);
        Direccion direccion = new Direccion();
        direccion.setId(dirId);

        Huesped existente = new Huesped();
        existente.setDni(111);

        when(direccionService.direccionExists(any(), any(), any(), any(), any()))
                .thenReturn(true);
        when(direccionRepository.findById(dirId))
                .thenReturn(Optional.of(direccion));

        when(huespedRepository.existsByTipoDniAndDni("DNI", 111))
                .thenReturn(true);
        when(huespedRepository.findByTipoDniAndDni("DNI", 111))
                .thenReturn(Optional.of(existente));
        when(huespedRepository.save(any()))
                .thenReturn(existente);

        Integer dni = huespedService.crearHuespedDTO(dto);

        // Assert
        assertEquals(111, dni);
        verify(telefonoRepository, never()).save(any());
    }

    @Test
    void modificarHuespedDTO_direccionNoExiste() {
        HuespedDTO dto = new HuespedDTO();
        dto.setNombre("Ana");
        dto.setApellido("Lopez");
        dto.setEmail("a@mail.com");

        DireccionDTO dirDTO = new DireccionDTO();
        dirDTO.setCalle("Nueva");
        dirDTO.setNumero(10);
        dirDTO.setDepartamento("-");
        dirDTO.setPiso(1);
        dirDTO.setCodPostal(2000);
        dto.setDireccion(dirDTO);

        DireccionId dirId = new DireccionId("Nueva", 10, "-", 1, 2000);
        Direccion direccion = new Direccion();
        direccion.setId(dirId);

        Huesped existente = new Huesped();
        existente.setDni(1);

        when(huespedRepository.findById(1))
                .thenReturn(Optional.of(existente));
        when(direccionService.mapToEntDireccion(any()))
                .thenReturn(direccion);
        when(direccionService.direccionExists(any(), any(), any(), any(), any()))
                .thenReturn(false);
        when(direccionRepository.findById(dirId))
                .thenReturn(Optional.of(direccion));

        // Act
        huespedService.modificarHuespedDTO("DNI", 1, dto);

        // Assert
        verify(huespedRepository).save(existente);
        verify(telefonoRepository).save(any(Telefono.class));
    }

    @Test
    void obtenerTodosDTO_devuelveListaDTO() {
        Huesped h = new Huesped();
        h.setDni(1);
        h.setNombre("Juan");
        h.setApellido("Perez");

        Direccion dir = new Direccion();
        DireccionId dirId = new DireccionId("Calle", 1, "-", 0, 1000);
        dir.setId(dirId);
        h.setDireccion(dir);

        when(huespedRepository.findAll())
                .thenReturn(List.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
                .thenReturn(new DireccionDTO());

        List<HuespedDTO> result = huespedService.obtenerTodosDTO();

        assertEquals(1, result.size());
        verify(huespedRepository).findAll();
    }

    @Test
    void buscarHuespedDTOByTipoDniAndDni_devuelveDTO() {
        Huesped h = new Huesped();
        h.setDni(123);
        h.setNombre("Ana");
        h.setApellido("Lopez");

        Direccion dir = new Direccion();
        DireccionId id = new DireccionId("Calle", 10, "-", 1, 2000);
        dir.setId(id);
        h.setDireccion(dir);

        when(huespedRepository.findByTipoDniAndDni("DNI", 123))
                .thenReturn(Optional.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
                .thenReturn(new DireccionDTO());

        HuespedDTO dto =
            huespedService.buscarHuespedDTOByTipoDniAndDni("DNI", 123);

        assertEquals(123, dto.getDni());
    }

    @Test
    void obtenerTodos_devuelveLista() {
        when(huespedRepository.findAll()).thenReturn(List.of());
        assertNotNull(huespedService.obtenerTodos());
    }

    @Test
    void getByApellidoDTO_devuelveLista() {
        Huesped h = new Huesped();
        h.setNombre("Juan");
        h.setApellido("Perez");
        h.setDni(1);

        Direccion dir = new Direccion();
        DireccionId id = new DireccionId("Calle", 1, "-", 0, 1000);
        dir.setId(id);
        h.setDireccion(dir);

        when(huespedRepository.findByApellidoStartingWithIgnoreCase("Perez"))
                .thenReturn(List.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
                .thenReturn(new DireccionDTO());

        List<HuespedDTO> lista =
                huespedService.getByApellidoDTO("Perez");

        assertEquals(1, lista.size());
        verify(huespedRepository).findByApellidoStartingWithIgnoreCase("Perez");
    }

    @Test
    void getByNombreDTO_devuelveLista() {
        Huesped h = new Huesped();
        h.setNombre("Ana");
        h.setApellido("Lopez");
        h.setDni(2);

        Direccion dir = new Direccion();
        DireccionId id = new DireccionId("Av", 10, "-", 1, 2000);
        dir.setId(id);
        h.setDireccion(dir);

        when(huespedRepository.findByNombreStartingWithIgnoreCase("Ana"))
                .thenReturn(List.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
                .thenReturn(new DireccionDTO());

        List<HuespedDTO> lista =
                huespedService.getByNombreDTO("Ana");

        assertEquals(1, lista.size());
        verify(huespedRepository).findByNombreStartingWithIgnoreCase("Ana");
    }

    @Test
    void buscarHuespedDTOPorTipoDni_devuelveLista() {
        Huesped h = new Huesped();
        h.setTipoDni("DNI");
        h.setDni(123);

        Direccion dir = new Direccion();
        DireccionId id = new DireccionId("Calle", 5, "-", 0, 1000);
        dir.setId(id);
        h.setDireccion(dir);

        when(huespedRepository.findByTipoDni("DNI"))
                .thenReturn(List.of(h));

        when(direccionService.mapToDTODireccion(any(), any()))
                .thenReturn(new DireccionDTO());

        List<HuespedDTO> lista =
                huespedService.buscarHuespedDTOPorTipoDni("DNI");

        assertEquals(1, lista.size());
    }
}

