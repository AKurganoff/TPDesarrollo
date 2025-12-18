package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Consumible;
import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;
import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Factura;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Juridica;
import com.Diseno.TPDiseno2025.Domain.ResponsablePago;
import com.Diseno.TPDiseno2025.Model.ConsumibleDTO;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Model.FacturaPreviewDTO;
import com.Diseno.TPDiseno2025.Model.FacturaRequestDTO;
import com.Diseno.TPDiseno2025.Model.JuridicaDTO;
import com.Diseno.TPDiseno2025.Model.ResponsablePagoDTO;
import com.Diseno.TPDiseno2025.Repository.ConsumibleRepository;
import com.Diseno.TPDiseno2025.Repository.DetalleEstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.EstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.FacturaRepository;
import com.Diseno.TPDiseno2025.Repository.JuridicaRepository;
import com.Diseno.TPDiseno2025.Repository.ResponsablePagoRepository;

@Service
public class FacturaServiceImp implements FacturaService {
    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private DetalleEstadiaRepository detalleRepository;

    @Autowired
    private ConsumibleRepository consumibleRepository;

    @Autowired
    private ResponsablePagoRepository responsableRepository;

    @Autowired
    private JuridicaRepository juridicaRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public List<Huesped> iniciarCheckout(
            Integer idHabitacion,
            LocalTime horaCheckout) {

        if (horaCheckout == null) {
            throw new RuntimeException("Debe ingresar la hora de check-out");
        }

        Estadia estadia = estadiaRepository.findByReserva_Habitacion_IdHabitacion(idHabitacion)
        .orElseThrow(() -> new RuntimeException("La habitación no está ocupada"));
        estadia.setHoraCheckOut(horaCheckout);
        estadiaRepository.save(estadia);

        return detalleRepository.findByEstadia_IdEstadia(estadia.getIdEstadia()).stream().map(DetalleEstadia::getHuesped)
                .toList();
    }

    @Override
    public ResponsablePago definirResponsablePago(Integer idEstadia, ResponsablePagoDTO responsableDTO) {

        estadiaRepository.findById(idEstadia).orElseThrow(() -> new RuntimeException("Estadía inexistente"));

        if (responsableDTO.getJuridica() != null) {

            JuridicaDTO juridicaDTO = responsableDTO.getJuridica();

            Juridica juridica = juridicaRepository.findById(juridicaDTO.getCuit()).orElseGet(() -> {
                        Juridica nueva = new Juridica();
                        nueva.setCuit(juridicaDTO.getCuit());
                        nueva.setRazonSocial(juridicaDTO.getRazonSocial());
                        nueva.setDireccion(mapDireccion(juridicaDTO.getDireccion()));
                        return juridicaRepository.save(nueva);
                    });

            ResponsablePago responsable = new ResponsablePago();
            responsable.setJuridica(juridica);
            responsable.setRazonSocial(juridica.getRazonSocial());

            return responsableRepository.save(responsable);
        }
        Huesped huesped = detalleRepository.findByEstadia_IdEstadia(idEstadia).stream().map(DetalleEstadia::getHuesped)
                .filter(h -> h.getDni().equals(responsableDTO.getIdResponsablePago()))
                .findFirst().orElseThrow(() -> new RuntimeException("El responsable debe ser un huésped"));

        if (calcularEdad(huesped.getFechaNacimiento()) < 18) {
            throw new RuntimeException("La persona seleccionada es menor de edad. Por favor elija otra");
        }

        ResponsablePago responsable = new ResponsablePago();
        responsable.setRazonSocial(huesped.getNombre() + " " + huesped.getApellido());

        return responsableRepository.save(responsable);
    }
    @Override
    public FacturaPreviewDTO obtenerPreview(Integer idEstadia,Integer idResponsablePago) {

        Estadia estadia = estadiaRepository.findById(idEstadia).orElseThrow();

        ResponsablePago responsable = responsableRepository.findById(idResponsablePago).orElseThrow();

        List<Consumible> consumibles = consumibleRepository.findByIdEstadia_IdEstadia(idEstadia);

        double totalConsumibles = consumibles.stream().mapToDouble(Consumible::getPrecio).sum();

        FacturaPreviewDTO preview = new FacturaPreviewDTO();
        preview.setPrecioEstadia(estadia.getPrecio());
        preview.setConsumibles(consumibles.stream().map(this::toDTO).toList());
        preview.setTotal(estadia.getPrecio() + totalConsumibles);
        preview.setTipoFactura(calcularTipoFactura(responsable));

        return preview;
    }
    @Override
    public Factura generarFactura(FacturaRequestDTO request) {

        Estadia estadia = estadiaRepository
                .findById(request.getIdEstadia())
                .orElseThrow();

        ResponsablePago responsable = definirResponsablePago(
                estadia.getIdEstadia(),
                request.getResponsablePago());

        List<Consumible> consumibles =
                consumibleRepository.findByIdEstadia_IdEstadia(estadia.getIdEstadia())
                        .stream()
                        .filter(c -> request.getConsumiblesSeleccionados()
                                .contains(c.getIdConsumible()))
                        .toList();

        double total = 0;
        if (Boolean.TRUE.equals(request.getIncluirEstadia())) {
            total += estadia.getPrecio();
        }

        total += consumibles.stream()
                .mapToDouble(Consumible::getPrecio)
                .sum();

        Factura factura = new Factura();
        factura.setIdEstadia(estadia);
        factura.setIdResponsablePago(responsable);
        factura.setPrecioFinal(total);
        factura.setFecha(LocalDate.now());
        factura.setTipoFactura(
                calcularTipoFactura(responsable));
        factura.setNroFactura(
                facturaRepository.countByFecha(LocalDate.now()) + 1);

        return facturaRepository.save(factura);
    }

    private int calcularEdad(LocalDate fecha) {
        return Period.between(fecha, LocalDate.now()).getYears();
    }

    private String calcularTipoFactura(ResponsablePago responsable) {
        return responsable.getJuridica() != null ? "A" : "B";
    }

    private ConsumibleDTO toDTO(Consumible c) {
        ConsumibleDTO dto = new ConsumibleDTO();
        dto.setIdConsumible(c.getIdConsumible());
        dto.setNombre(c.getNombre());
        dto.setPrecio(c.getPrecio());
        return dto;
    }

    private Direccion mapDireccion(DireccionDTO dto) {

        DireccionId id = new DireccionId(
                dto.getCalle(),
                dto.getNumero(),
                dto.getDepartamento(),
                dto.getPiso(),
                dto.getCodPostal()
        );

        Direccion d = new Direccion();
        d.setId(id);
        d.setLocalidad(dto.getLocalidad());
        d.setProvincia(dto.getProvincia());
        d.setPais(dto.getPais());

        return d;
    }
}
