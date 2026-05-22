package Proyecto.EpresSmart.Services;

import Proyecto.EpresSmart.Repository.HabitacionRepository;
import Proyecto.EpresSmart.modelos.Habitacion;
import Proyecto.EpresSmart.modelos.HabitacionComodidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    public List<Habitacion> obtenerTodas() {
        return habitacionRepository.findAll();
    }

    /**
     * BUG FIX: estado normalizado a "Disponible" con D mayuscula.
     * En MongoDB era "disponible" (minuscula) y el Angular mostraba tarjetas vacias.
     */
    public List<Habitacion> obtenerDisponibles() {
        return habitacionRepository.findByEstado("Disponible");
    }

    /**
     * BUG FIX: al guardar habitacion con comodidades (List<HabitacionComodidad>),
     * hay que asociar cada comodidad a la habitacion antes de persistir,
     * si no Hibernate lanza "detached entity" o guarda sin FK.
     */
    @Transactional
    public Habitacion guardarHabitacion(Habitacion habitacion) {
        if (habitacion.getEstado() == null || habitacion.getEstado().isBlank()) {
            habitacion.setEstado("Disponible");
        }
        if (habitacion.getComodidades() != null) {
            for (HabitacionComodidad c : habitacion.getComodidades()) {
                c.setHabitacion(habitacion);
            }
        }
        return habitacionRepository.save(habitacion);
    }

    @Transactional
    public Optional<Habitacion> actualizar(Long id, Habitacion datos) {
        return habitacionRepository.findById(id).map(h -> {
            h.setNumero(datos.getNumero());
            h.setTipo(datos.getTipo());
            h.setPrecio(datos.getPrecio());
            h.setEstado(datos.getEstado());
            // Reemplaza lista de comodidades limpiando las antiguas (orphanRemoval)
            h.getComodidades().clear();
            if (datos.getComodidades() != null) {
                for (HabitacionComodidad c : datos.getComodidades()) {
                    c.setHabitacion(h);
                    h.getComodidades().add(c);
                }
            }
            return habitacionRepository.save(h);
        });
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
