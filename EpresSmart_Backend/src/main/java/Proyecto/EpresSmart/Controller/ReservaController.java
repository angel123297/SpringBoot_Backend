package Proyecto.EpresSmart.Controller;

import Proyecto.EpresSmart.Services.ReservaService;
import Proyecto.EpresSmart.modelos.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaService.obtenerTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Reserva> reservasPorUsuario(@PathVariable Long usuarioId) {
        return reservaService.obtenerPorUsuario(usuarioId);
    }

    /**
     * FIX: Recibe Map para mapear correctamente los campos del frontend:
     *   habitacionId, checkIn, checkOut, usuarioId (opcional), nombre, email, telefono
     */
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Map<String, Object> body) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reservaService.crearReserva(body));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id,
                                       @RequestBody(required = false) Map<String, String> body) {
        String admin = (body != null) ? body.getOrDefault("adminUsername", "admin") : "admin";
        return reservaService.confirmar(id, admin)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(
                        Map.of("mensaje", "Reserva confirmada", "id", r.getId())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reserva no encontrada")));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id,
                                      @RequestBody Map<String, String> body) {
        String admin  = body.getOrDefault("adminUsername", "admin");
        String motivo = body.getOrDefault("motivo", "");
        return reservaService.rechazar(id, motivo, admin)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(Map.of("mensaje", "Reserva rechazada")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reserva no encontrada")));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        return reservaService.cancelar(id)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reserva no encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (reservaService.eliminar(id))
            return ResponseEntity.ok(Map.of("mensaje", "Reserva eliminada"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Reserva no encontrada"));
    }
}
