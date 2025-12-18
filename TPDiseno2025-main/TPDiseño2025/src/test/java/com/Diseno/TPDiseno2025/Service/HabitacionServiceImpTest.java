package com.Diseno.TPDiseno2025.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HabitacionServiceImpTest {

    @InjectMocks
    private HabitacionServiceImp habitacionService;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private TipoHabitacionService tipoHabitacionService;

    @Mock
    private DetalleReservaService detalleReservaService;

    private Habitacion habitacion;
    private TipoHabitacion tipo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tipo = new TipoHabitacion();
        tipo.setIdTipo(1);

        habitacion = new Habitacion();
        habitacion.setIdHabitacion(101);
        habitacion.setIdTipo(tipo);
        habitacion.setEstado("disponible");
        habitacion.setNochesDescuento(2);
    }

    @Test
    void testCrearHabitacion() {
        when(tipoHabitacionService.getTipoByIdTipo(1)).thenReturn(tipo);
        habitacionService.crearHabitacion(101, 1, 2, "disponible");
        verify(habitacionRepository, times(1)).save(any(Habitacion.class));
    }

    @Test
    void testMapToDTOHabitacion() {
        HabitacionDTO dto = habitacionService.mapToDTOHabitacion(habitacion);
        assertEquals(101, dto.getIdHabitacion());
        assertEquals(1, dto.getIdTipo());
        assertEquals("disponible", dto.getEstado());
        assertEquals(2, dto.getNochesDescuento());
    }

    @Test
    void testMapToEntHabitacion() {
        HabitacionDTO dto = habitacionService.mapToDTOHabitacion(habitacion);
        when(tipoHabitacionService.getTipoByIdTipo(1)).thenReturn(tipo);
        Habitacion ent = habitacionService.mapToEntHabitacion(dto);
        assertEquals(101, ent.getIdHabitacion());
        assertEquals(tipo, ent.getIdTipo());
        assertEquals("disponible", ent.getEstado());
    }

    @Test
    void testObtenerTodasPorTipo_Null() {
        List<Habitacion> lista = new ArrayList<>();
        lista.add(habitacion);
        when(habitacionRepository.findAll()).thenReturn(lista);
        List<Habitacion> result = habitacionService.obtenerTodasPorTipo(null);
        assertEquals(1, result.size());
    }

    @Test
    void testVerificarEstadoHabitacion_Disponible() {
        when(habitacionRepository.existsById(101)).thenReturn(true);
        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));
        assertTrue(habitacionService.verificarEstadoHabitacion(101));
    }

    @Test
    void testVerificarEstadoHabitacion_NoDisponible() {
        when(habitacionRepository.existsById(101)).thenReturn(false);
        assertFalse(habitacionService.verificarEstadoHabitacion(101));
    }

    @Test
    void testReservarHabitacion_Exitosa() {
        when(habitacionRepository.existsById(101)).thenReturn(true);
        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));
        habitacionService.reservarHabitacion(101);
        assertEquals("reservada", habitacion.getEstado());
    }

    @Test
    void testReservarHabitacion_Falla() {
        when(habitacionRepository.existsById(101)).thenReturn(true);
        habitacion.setEstado("ocupada");
        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            habitacionService.reservarHabitacion(101);
        });
        assertEquals("Habitacion no disponible", exception.getMessage());
    }

    @Test
    void testObtenerTodas() {
        List<Habitacion> lista = new ArrayList<>();
        lista.add(habitacion);

        when(habitacionRepository.findAll()).thenReturn(lista);

        List<Habitacion> result = habitacionService.obtenerTodas();
        assertEquals(1, result.size());
    }


    @Test
    void testObtenerTodasPorTipo_ConTipo() {
        List<Habitacion> lista = new ArrayList<>();
        lista.add(habitacion);

        when(tipoHabitacionService.getTipoByIdTipo(1)).thenReturn(tipo);
        when(habitacionRepository.findByIdTipo(tipo)).thenReturn(lista);

        List<Habitacion> result = habitacionService.obtenerTodasPorTipo(1);
        assertEquals(1, result.size());
    }

    @Test
    void testMostrarEstadoHabitacionesByFecha() {
        List<Habitacion> lista = new ArrayList<>();
        lista.add(habitacion);

        when(habitacionRepository.findAll()).thenReturn(lista);

        List<String> estados = habitacionService
                .mostrarestadoHabitacionesByFecha("2024-01-01", "2024-01-05");

        assertEquals(1, estados.size());
        assertEquals("disponible", estados.get(0));
    }

    @Test
    void testBuscarHabitacionByIdHabitacion() {
        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));

        Habitacion result = habitacionService.buscarHabitacionByIdHabitacion(101);
        assertEquals(101, result.getIdHabitacion());
    }


    @Test
    void testBuscarHabitacionDTOByIdHabitacion() {
        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));

        HabitacionDTO dto = habitacionService.buscarHabitacionDTOByIdHabitacion(101);

        assertEquals(101, dto.getIdHabitacion());
        assertEquals("disponible", dto.getEstado());
    }

    @Test
    void testObtenerTodasPorTipo_SinDependenciaDetalleReserva() {
        
        Habitacion hab1 = new Habitacion();
        hab1.setIdHabitacion(101);
        hab1.setIdTipo(tipo);
        hab1.setEstado("disponible");

        Habitacion hab2 = new Habitacion();
        hab2.setIdHabitacion(102);
        hab2.setIdTipo(tipo);
        hab2.setEstado("ocupada");

        List<Habitacion> lista = new ArrayList<>();
        lista.add(hab1);
        lista.add(hab2);

        when(tipoHabitacionService.getTipoByIdTipo(1)).thenReturn(tipo);
        when(habitacionRepository.findByIdTipo(tipo)).thenReturn(lista);

        List<Habitacion> result = habitacionService.obtenerTodasPorTipo(1);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(h -> h.getIdHabitacion() == 101));
        assertTrue(result.stream().anyMatch(h -> h.getIdHabitacion() == 102));
    }

    @Test
    void testHabitacionDisponibleEnFechas_Disponible() {
        // Mock: no hay reservas en conflicto
        when(detalleReservaService.buscarReservasEnConflicto(any(), any()))
                .thenReturn(new ArrayList<>());

        boolean disponible = habitacionService.habitacionDisponibleEnFechas(
                101, LocalDate.now(), LocalDate.now().plusDays(2));

        assertTrue(disponible);
    }

}
