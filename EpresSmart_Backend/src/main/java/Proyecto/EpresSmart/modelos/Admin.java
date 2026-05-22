package Proyecto.EpresSmart.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin independiente de Usuario — misma logica del original.
 * BUG FIX: @Document -> @Entity, String id -> Long id.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "administradores")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
