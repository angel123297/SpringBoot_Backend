package Proyecto.EpresSmart.Services;

import Proyecto.EpresSmart.Repository.HabitacionRepository;
import Proyecto.EpresSmart.Repository.ReservaRepository;
import Proyecto.EpresSmart.Repository.UsuarioRepository;
import Proyecto.EpresSmart.modelos.Habitacion;
import Proyecto.EpresSmart.modelos.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private HabitacionRepository habitacionRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Reserva> obtenerTodas() { return reservaRepository.findAll(); }

    public List<Reserva> obtenerPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public Reserva crearReserva(Map<String, Object> data) {
        Number habId = (Number) data.get("habitacionId");
        if (habId == null) throw new RuntimeException("Habitación requerida");

        Habitacion habitacion = habitacionRepository.findById(habId.longValue())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada: " + habId));

        String checkIn  = (String) data.get("checkIn");
        String checkOut = (String) data.get("checkOut");
        if (checkIn == null || checkOut == null)
            throw new RuntimeException("Las fechas de check-in y check-out son requeridas");

        Reserva reserva = new Reserva();
        reserva.setNombre((String) data.get("nombre"));
        reserva.setEmail((String) data.get("email"));
        reserva.setTelefono((String) data.get("telefono"));
        reserva.setPais((String) data.get("pais"));
        reserva.setHabitacion(habitacion);
        reserva.setFechaCheckIn(LocalDate.parse(checkIn));
        reserva.setFechaCheckOut(LocalDate.parse(checkOut));
        reserva.setEstado("Sin Confirmar");

        Number uid = (Number) data.get("usuarioId");
        if (uid != null) {
            usuarioRepository.findById(uid.longValue()).ifPresent(reserva::setUsuario);
        }

        habitacion.setEstado("Ocupada");
        habitacionRepository.save(habitacion);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Optional<Reserva> confirmar(Long id, String adminUsername) {
        return reservaRepository.findById(id).map(r -> {
            r.setEstado("Confirmada");
            r.setAprobadoPor(adminUsername);
            return reservaRepository.save(r);
        });
    }

    @Transactional
    public Optional<Reserva> rechazar(Long id, String motivo, String adminUsername) {
        return reservaRepository.findById(id).map(r -> {
            r.setEstado("Rechazada");
            r.setMotivoRechazo(motivo);
            r.setAprobadoPor(adminUsername);
            Habitacion h = r.getHabitacion();
            if (h != null) { h.setEstado("Disponible"); habitacionRepository.save(h); }
            return reservaRepository.save(r);
        });
    }

    @Transactional
    public Optional<Reserva> cancelar(Long id) {
        return reservaRepository.findById(id).map(r -> {
            r.setEstado("Cancelada");
            Habitacion h = r.getHabitacion();
            if (h != null) { h.setEstado("Disponible"); habitacionRepository.save(h); }
            return reservaRepository.save(r);
        });
    }

    @Transactional
    public boolean eliminar(Long id) {
        return reservaRepository.findById(id).map(r -> {
            // Liberar la habitación al eliminar la reserva
            Habitacion h = r.getHabitacion();
            if (h != null) { h.setEstado("Disponible"); habitacionRepository.save(h); }
            reservaRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}
