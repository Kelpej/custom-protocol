package ukma.edu.ua.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "manufacturer", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
