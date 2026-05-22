package Proyecto.EpresSmart.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * BUG FIX: @DBRef -> @ManyToOne JPA.
 * reserva puede ser null (opinion general sin reserva asociada).
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "opiniones")
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    private LocalDateTime fechaEnvio;
}
