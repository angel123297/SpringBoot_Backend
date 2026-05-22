package Proyecto.EpresSmart.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/api")
    public String pruebaApi() {
        return "EpresSmart API funcionando correctamente (MySQL)";
    }
}
