package Proyecto.EpresSmart.Services;

import Proyecto.EpresSmart.Repository.OpinionRepository;
import Proyecto.EpresSmart.Repository.UsuarioRepository;
import Proyecto.EpresSmart.modelos.Opinion;
import Proyecto.EpresSmart.modelos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OpinionService {

    @Autowired private OpinionRepository opinionRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Opinion> obtenerTodas() {
        return opinionRepository.findAll();
    }

    public List<Opinion> obtenerPorUsuario(Long usuarioId) {
        return opinionRepository.findByUsuarioId(usuarioId);
    }

    /**
     * BUG FIX: opinion puede llegar sin usuario.id (mensaje de contacto publico).
     * Se acepta igualmente; solo se valida si hay id.
     */
    @Transactional
    public Opinion guardarOpinion(Opinion opinion) {
        if (opinion.getUsuario() != null && opinion.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(opinion.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            opinion.setUsuario(usuario);
        }
        opinion.setFechaEnvio(LocalDateTime.now());
        return opinionRepository.save(opinion);
    }
}
