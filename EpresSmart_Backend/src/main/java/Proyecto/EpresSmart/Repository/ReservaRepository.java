package Proyecto.EpresSmart.Repository;

import Proyecto.EpresSmart.modelos.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // BUG FIX: findByUsuarioId ahora usa Long (antes String de MongoDB ObjectId)
    List<Reserva> findByUsuarioId(Long usuarioId);
}
