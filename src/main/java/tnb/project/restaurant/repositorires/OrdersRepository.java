package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.Orders;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,String> {
    List<Orders> findBySession_Id(String sessionId);
}
