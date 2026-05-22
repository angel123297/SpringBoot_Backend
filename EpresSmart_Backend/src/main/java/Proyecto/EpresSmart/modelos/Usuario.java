package Proyecto.EpresSmart.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BUG FIX: se reemplaza @Document MongoDB por @Entity JPA.
 * Se agrega @NoArgsConstructor requerido por Hibernate.
 * @Column(unique=true) en lugar de @Indexed de Mongo.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String telefono;
}
