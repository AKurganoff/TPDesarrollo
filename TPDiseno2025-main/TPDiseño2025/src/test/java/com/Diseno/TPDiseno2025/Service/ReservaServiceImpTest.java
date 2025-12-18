package com.Diseno.TPDiseno2025.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Diseno.TPDiseno2025.Domain.*;
import com.Diseno.TPDiseno2025.Model.*;
import com.Diseno.TPDiseno2025.Repository.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImpTest {

    @InjectMocks
    private ReservaServiceImp reservaService;

    @Mock
    private HuespedService huespedService;

    @Mock
    private HabitacionService habitacionService;

    @Mock
    private DetalleReservaService detalleService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private DetalleReservaRepository detalleReservaRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    private ReservaDTO reservaDTO;
    private HuespedDTO huespedDTO;
    private HabitacionDTO habitacionDTO;

    @BeforeEach
    void setUp() {
        reservaDTO = new ReservaDTO();
        reservaDTO.setFechaInicio("2025-01-01");
        reservaDTO.setCantNoches(2);
        reservaDTO.setCantHuesped(2);
        reservaDTO.setEstado("Confirmada");

        huespedDTO = new HuespedDTO();
        huespedDTO.setTipoDni("DNI");
        huespedDTO.setDni(123);

        habitacionDTO = new HabitacionDTO();
        habitacionDTO.setIdHabitacion(1);
    }

    @Test
    void crearReserva_ok() {
        Huesped huesped = new Huesped();
        Habitacion hab = mock(Habitacion.class);
        TipoHabitacion tipo = mock(TipoHabitacion.class);

        when(tipo.getPrecioNoche()).thenReturn(100.0);
        when(hab.getIdTipo()).thenReturn(tipo);

        when(habitacionService.habitacionDisponibleEnFechas(anyInt(), any(), any()))
                .thenReturn(true);
        when(huespedService.buscarHuespedByTipoDniAndDni(any(), any()))
                .thenReturn(huesped);
        when(habitacionService.buscarHabitacionByIdHabitacion(anyInt()))
                .thenReturn(hab);

        reservaService.crearReserva(reservaDTO, huespedDTO, habitacionDTO);

        verify(reservaRepository).save(any(Reserva.class));
        verify(detalleService).guardarDetalle(any(DetalleReserva.class));
    }

    @Test
    void crearReserva_habitacionNoDisponible_lanzaExcepcion() {
        when(habitacionService.habitacionDisponibleEnFechas(anyInt(), any(), any()))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.crearReserva(reservaDTO, huespedDTO, habitacionDTO));

        verify(reservaRepository, never()).save(any());
    }

    @Test
    void modificarReserva_ok() {
        Reserva existente = new Reserva();
        existente.setIdReserva(1);

        Reserva actualizada = new Reserva();
        actualizada.setCantHuesped(3);
        actualizada.setCantNoches(5);
        actualizada.setFechaInicio(LocalDate.now());

        when(reservaRepository.findById(1)).thenReturn(Optional.of(existente));

        reservaService.modificarReserva(1, actualizada);

        verify(reservaRepository).save(existente);
        assertEquals(3, existente.getCantHuesped());
    }

    @Test
    void eliminarReserva_ok() {
        Reserva r = new Reserva();
        reservaService.eliminarReserva(r);
        verify(reservaRepository).delete(r);
    }

    @Test
    void obtenerReservaPorId_existe() {
        Reserva r = new Reserva();
        when(reservaRepository.findById(1)).thenReturn(Optional.of(r));

        Reserva resultado = reservaService.obtenerReservaPorId(1);

        assertNotNull(resultado);
    }

    @Test
    void obtenerReservaPorId_noExiste() {
        when(reservaRepository.findById(1)).thenReturn(Optional.empty());
        assertNull(reservaService.obtenerReservaPorId(1));
    }

    @Test
    void mapToEntity_ok() {
        Reserva r = reservaService.mapToEntity(reservaDTO);
        assertEquals(2, r.getCantNoches());
        assertEquals(LocalDate.parse("2025-01-01"), r.getFechaInicio());
    }

    @Test
    void mapToDTO_ok() {
        Reserva r = new Reserva();
        r.setIdReserva(1);
        r.setCantHuesped(2);
        r.setCantNoches(2);
        r.setFechaInicio(LocalDate.now());
        r.setEstado("Confirmada");

        Huesped h = new Huesped();
        h.setDni(123);
        r.setHuesped(h);

        ReservaDTO dto = reservaService.mapToDTO(r);

        assertEquals(123, dto.getIdHuesped());
    }

    @Test
    void buscarReservas_conNombre() {
        Reserva r = new Reserva();
        r.setIdReserva(1);

        Huesped h = new Huesped();
        h.setNombre("Juan");
        h.setApellido("Perez");
        r.setHuesped(h);
        r.setFechaInicio(LocalDate.now());
        r.setCantNoches(2);

        DetalleReserva d = new DetalleReserva();
        Habitacion hab = new Habitacion();
        hab.setIdHabitacion(10);
        d.setHabitacion(hab);

        when(reservaRepository.findByHuesped_NombreAndHuesped_ApellidoIgnoreCase(any(), any()))
                .thenReturn(List.of(r));
        when(detalleReservaRepository.findByReserva_IdReserva(anyInt()))
                .thenReturn(List.of(d));

        List<ReservaListadoDTO> lista = reservaService.buscarReservas("Perez", "Juan");

        assertFalse(lista.isEmpty());
    }

    @Test
    void cancelarReserva_ok() {
        Reserva r = new Reserva();
        r.setIdReserva(1);
        r.setEstado("Confirmada");

        Habitacion h = new Habitacion();
        h.setEstado("Ocupada");

        DetalleReserva d = new DetalleReserva();
        d.setHabitacion(h);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(r));
        when(detalleReservaRepository.findByReserva_IdReserva(1))
                .thenReturn(List.of(d));

        reservaService.cancelarReserva(1);

        assertEquals("Cancelada", r.getEstado());
        verify(detalleReservaRepository).deleteAll(anyList());
        verify(habitacionRepository).save(any());
    }

    @Test
    void cancelarReserva_noExiste_lanzaExcepcion() {
        when(reservaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ReservaServiceImp.NotFoundException.class,
                () -> reservaService.cancelarReserva(1));
    }

    @Test
    void obtenerMatrizDisponibilidad_sinReservas_todoLibre() {

        Habitacion h1 = new Habitacion();
        h1.setIdHabitacion(1);

        when(habitacionService.obtenerTodasPorTipo(1))
                .thenReturn(List.of(h1));

        when(detalleService.buscarReservasEnConflicto(any(), any()))
                .thenReturn(List.of());

        List<CeldaCalendarioDTO> grilla =
                reservaService.obtenerMatrizDisponibilidad(
                        "2025-01-01",
                        "2025-01-02",
                        1
                );

        assertEquals(2, grilla.size()); // 2 días * 1 habitación
        assertEquals("LIBRE", grilla.get(0).getEstado());
    }

    @Test
    void obtenerMatrizDisponibilidad_reservaConfirmada_estadoReservada() {

        Habitacion h = new Habitacion();
        h.setIdHabitacion(1);

        Reserva r = new Reserva();
        r.setIdReserva(10);
        r.setEstado("Confirmada");
        r.setFechaInicio(LocalDate.parse("2025-01-01"));

        DetalleReserva d = new DetalleReserva();
        d.setHabitacion(h);
        d.setReserva(r);
        d.setCantidadNoches(2);

        when(habitacionService.obtenerTodasPorTipo(1))
                .thenReturn(List.of(h));

        when(detalleService.buscarReservasEnConflicto(any(), any()))
                .thenReturn(List.of(d));

        List<CeldaCalendarioDTO> grilla =
                reservaService.obtenerMatrizDisponibilidad(
                        "2025-01-01",
                        "2025-01-01",
                        1
                );

        CeldaCalendarioDTO celda = grilla.get(0);

        assertEquals("RESERVADA", celda.getEstado());
        assertEquals(10, celda.getIdReserva());
    }

    @Test
    void obtenerMatrizDisponibilidad_reservaNoConfirmada_estadoOcupada() {

        Habitacion h = new Habitacion();
        h.setIdHabitacion(1);

        Reserva r = new Reserva();
        r.setIdReserva(20);
        r.setEstado("Pendiente");
        r.setFechaInicio(LocalDate.parse("2025-01-01"));

        DetalleReserva d = new DetalleReserva();
        d.setHabitacion(h);
        d.setReserva(r);
        d.setCantidadNoches(1);

        when(habitacionService.obtenerTodasPorTipo(1))
                .thenReturn(List.of(h));

        when(detalleService.buscarReservasEnConflicto(any(), any()))
                .thenReturn(List.of(d));

        List<CeldaCalendarioDTO> grilla =
                reservaService.obtenerMatrizDisponibilidad(
                        "2025-01-01",
                        "2025-01-01",
                        1
                );

        assertEquals("OCUPADA", grilla.get(0).getEstado());
    }


}
