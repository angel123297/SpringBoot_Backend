package Proyecto.EpresSmart.Repository;

import Proyecto.EpresSmart.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}
