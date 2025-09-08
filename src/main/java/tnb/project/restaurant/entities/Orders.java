package tnb.project.restaurant.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @OneToOne
    @JoinColumn(name = "session_id")
    Session session;
    BigDecimal amount;
    Long loyaltyRedeemPoints;
    BigDecimal discountAmount;
    LocalDateTime createdAt;
    BigDecimal totalAmount;
    String status;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;
}
