package Proyecto.EpresSmart.Controller;

import Proyecto.EpresSmart.Repository.AdminRepository;
import Proyecto.EpresSmart.Repository.UsuarioRepository;
import Proyecto.EpresSmart.modelos.Admin;
import Proyecto.EpresSmart.modelos.Usuario;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AdminRepository adminRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    /**
     * Crea el admin por defecto al arrancar si no existe.
     * Credenciales: admin / admin123
     */
    @PostConstruct
    public void seedAdminPorDefecto() {
        if (!adminRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            adminRepository.save(admin);
            System.out.println("Admin por defecto creado: admin / admin123");
        }
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username y password son requeridos"));

        Admin adminDb = adminRepository.findByUsername(username);
        if (adminDb == null || !adminDb.getPassword().equals(password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));

        return ResponseEntity.ok(Map.of(
            "mensaje", "Login exitoso",
            "rol",     "admin",
            "id",      adminDb.getId(),
            "nombre",  adminDb.getUsername()
        ));
    }

    @PostMapping("/login/usuario")
    public ResponseEntity<?> loginUsuario(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email y password son requeridos"));

        Usuario usuarioDb = usuarioRepository.findByEmail(email);
        if (usuarioDb == null || !usuarioDb.getPassword().equals(password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));

        return ResponseEntity.ok(Map.of(
            "mensaje", "Login exitoso",
            "rol",     "usuario",
            "id",      usuarioDb.getId(),
            "nombre",  usuarioDb.getNombre(),
            "email",   usuarioDb.getEmail()
        ));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email es requerido"));
        if (usuarioRepository.existsByEmail(usuario.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El email ya esta registrado"));

        Usuario nuevo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Usuario registrado correctamente", "id", nuevo.getId()));
    }
}
