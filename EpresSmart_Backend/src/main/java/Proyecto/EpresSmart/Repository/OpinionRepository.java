package Proyecto.EpresSmart.Repository;

import Proyecto.EpresSmart.modelos.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    List<Opinion> findByUsuarioId(Long usuarioId);
}
