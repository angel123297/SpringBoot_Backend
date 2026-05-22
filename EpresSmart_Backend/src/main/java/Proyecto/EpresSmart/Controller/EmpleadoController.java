package Proyecto.EpresSmart.Controller;

import Proyecto.EpresSmart.Services.EmpleadoService;
import Proyecto.EpresSmart.modelos.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Empleado> listarEmpleados() {
        return empleadoService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<?> crearEmpleado(@RequestBody Empleado empleado) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(empleadoService.crear(empleado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al guardar el empleado: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoService.actualizar(id, empleado)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Empleado no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (empleadoService.eliminar(id))
            return ResponseEntity.ok(Map.of("mensaje", "Empleado eliminado"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Empleado no encontrado"));
    }
}
