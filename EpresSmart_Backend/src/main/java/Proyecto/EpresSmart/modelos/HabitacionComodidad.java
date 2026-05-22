package Proyecto.EpresSmart.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * En MongoDB las comodidades eran List<String> embebidas.
 * En MySQL necesitan tabla propia para cumplir 1FN.
 * Esta entidad representa cada comodidad de una habitacion.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "habitacion_comodidades")
public class HabitacionComodidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private String comodidad;
}
