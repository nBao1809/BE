package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.ServiceType;
@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {
}
