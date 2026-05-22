package Proyecto.EpresSmart.Services;

import Proyecto.EpresSmart.Repository.EmpleadoRepository;
import Proyecto.EpresSmart.Repository.UsuarioRepository;
import Proyecto.EpresSmart.modelos.Empleado;
import Proyecto.EpresSmart.modelos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    @Autowired private EmpleadoRepository empleadoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    /**
     * BUG FIX: el Angular envia el objeto usuario COMPLETO embebido en Empleado
     * (nombre, email, password, telefono) sin ID, porque es un usuario nuevo.
     * El original esperaba solo usuario.id (ObjectId de Mongo) y fallaba con null.
     *
     * Logica:
     *  - Si viene usuario.id -> busca en BD existente
     *  - Si no hay id -> guarda el usuario nuevo primero, luego el empleado
     */
    @Transactional
    public Empleado crear(Empleado empleado) {
        if (empleado.getUsuario() == null)
            throw new RuntimeException("Datos del usuario requeridos");

        Usuario usuario;
        if (empleado.getUsuario().getId() != null) {
            usuario = usuarioRepository.findById(empleado.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            if (empleadoRepository.existsByUsuarioId(usuario.getId()))
                throw new RuntimeException("El usuario ya es empleado");
        } else {
            // Usuario nuevo: valida campos obligatorios antes de persistir
            if (empleado.getUsuario().getNombre() == null || empleado.getUsuario().getNombre().isBlank())
                throw new RuntimeException("El nombre del empleado es requerido");
            if (empleado.getUsuario().getEmail() == null || empleado.getUsuario().getEmail().isBlank())
                throw new RuntimeException("El email del empleado es requerido");
            if (usuarioRepository.existsByEmail(empleado.getUsuario().getEmail()))
                throw new RuntimeException("El email ya está registrado en el sistema");
            if (empleado.getUsuario().getPassword() == null || empleado.getUsuario().getPassword().isBlank())
                empleado.getUsuario().setPassword("123456"); // password por defecto
            usuario = usuarioRepository.save(empleado.getUsuario());
        }

        empleado.setUsuario(usuario);
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Optional<Empleado> actualizar(Long id, Empleado datos) {
        return empleadoRepository.findById(id).map(e -> {
            e.setRol(datos.getRol());
            e.setTurno(datos.getTurno());
            return empleadoRepository.save(e);
        });
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
