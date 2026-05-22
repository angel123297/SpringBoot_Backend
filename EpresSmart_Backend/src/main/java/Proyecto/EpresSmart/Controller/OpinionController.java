package Proyecto.EpresSmart.Controller;

import Proyecto.EpresSmart.Services.OpinionService;
import Proyecto.EpresSmart.modelos.Opinion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/opiniones")
public class OpinionController {

    @Autowired
    private OpinionService opinionService;

    @GetMapping
    public List<Opinion> listarOpiniones() {
        return opinionService.obtenerTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Opinion> porUsuario(@PathVariable Long usuarioId) {
        return opinionService.obtenerPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Opinion opinion) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(opinionService.guardarOpinion(opinion));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
