package ukma.edu.ua.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
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
    private Manufacturer manufacturer;
    @ManyToOne
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
