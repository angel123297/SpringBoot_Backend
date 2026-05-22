package Proyecto.EpresSmart.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BUG FIX: @DBRef -> @ManyToOne JPA.
 * BUG FIX: EmpleadoService.crear() antes esperaba usuario.id (String ObjectId).
 *          Ahora el Angular envia el objeto usuario completo embebido;
 *          el service lo guarda en la tabla usuarios si es nuevo.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String turno;
}
