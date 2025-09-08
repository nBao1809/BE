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
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
            @JoinColumn(name = "session_id")
    Session session;
    @ManyToOne
    @JoinColumn(name = "service_type_id")
    ServiceType serviceType;
    LocalDateTime created_at;
    LocalDateTime handleTime;
    String status;
}
