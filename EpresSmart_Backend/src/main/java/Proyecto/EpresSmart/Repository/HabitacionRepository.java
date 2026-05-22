package Proyecto.EpresSmart.Repository;

import Proyecto.EpresSmart.modelos.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    // BUG FIX: en el original buscaba "disponible" (minuscula).
    // Ahora el estado normalizado es "Disponible" (capital).
    List<Habitacion> findByEstado(String estado);
    boolean existsByNumero(String numero);
}
