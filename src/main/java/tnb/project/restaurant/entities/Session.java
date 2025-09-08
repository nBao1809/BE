package tnb.project.restaurant.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne
    @JoinColumn(name = "table_id")
    Tables table;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String status;
}
