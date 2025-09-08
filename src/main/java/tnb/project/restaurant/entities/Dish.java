package tnb.project.restaurant.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(scale = 2)
    BigDecimal price;
    String imageUrl;
    String status;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @ManyToMany
    @JoinTable(
            name = "dish_option",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "option_detail_id")
    )
    Set<OptionDetail> options;
}
