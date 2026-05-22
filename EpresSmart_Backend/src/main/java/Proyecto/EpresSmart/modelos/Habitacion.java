package Proyecto.EpresSmart.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * BUG FIX: List<String> comodidades -> @OneToMany a HabitacionComodidad (1FN en MySQL).
 * BUG FIX: estado "disponible" en minusculas causaba que el Angular no mostrara nada
 *          porque comparaba con "Disponible". Se normaliza en el Service.
 * cascade + orphanRemoval permite agregar/quitar comodidades junto con la habitacion.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private String estado = "Disponible";

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<HabitacionComodidad> comodidades = new ArrayList<>();
}
