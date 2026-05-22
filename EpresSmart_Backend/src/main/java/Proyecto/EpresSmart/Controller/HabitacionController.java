package Proyecto.EpresSmart.Controller;

import Proyecto.EpresSmart.Services.HabitacionService;
import Proyecto.EpresSmart.modelos.Habitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @GetMapping
    public List<Habitacion> listarTodas() {
        return habitacionService.obtenerTodas();
    }

    @GetMapping("/disponibles")
    public List<Habitacion> listarDisponibles() {
        return habitacionService.obtenerDisponibles();
    }

    @PostMapping
    public ResponseEntity<Habitacion> crearHabitacion(@RequestBody Habitacion habitacion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(habitacionService.guardarHabitacion(habitacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarHabitacion(@PathVariable Long id, @RequestBody Habitacion habitacion) {
        return habitacionService.actualizar(id, habitacion)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Habitacion no encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHabitacion(@PathVariable Long id) {
        if (habitacionService.eliminar(id))
            return ResponseEntity.ok(Map.of("mensaje", "Habitacion eliminada"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Habitacion no encontrada"));
    }
}
