package ukma.edu.ua.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String description;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Manufacturer manufacturer;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Group group;
    private int quantity;
    private double price;

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Manufacturer manufacturer() {
        return manufacturer;
    }

    public int quantity() {
        return quantity;
    }

    public double price() {
        return price;
    }
}
