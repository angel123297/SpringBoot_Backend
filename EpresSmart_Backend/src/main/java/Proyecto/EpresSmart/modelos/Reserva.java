package Proyecto.EpresSmart.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del huésped
    private String nombre;
    private String email;
    private String telefono;
    private String pais;       // ← nuevo campo

    // Usuario registrado — opcional
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @JsonProperty("checkIn")
    @Column(nullable = false)
    private LocalDate fechaCheckIn;

    @JsonProperty("checkOut")
    @Column(nullable = false)
    private LocalDate fechaCheckOut;

    @Column(nullable = false)
    private String estado = "Sin Confirmar";

    @JsonProperty("motivo")
    private String motivoRechazo;

    private String aprobadoPor;
}
