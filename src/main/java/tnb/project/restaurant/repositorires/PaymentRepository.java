package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
    // Thống kê doanh thu theo ngày
    @Query("SELECT DATE(p.paidAt) as date, SUM(p.amount) as total FROM Payment p WHERE p.paidAt BETWEEN :from AND :to AND p.status = 'SUCCESS' GROUP BY DATE(p.paidAt) ORDER BY date")
    List<Object[]> getRevenueByDay(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Thống kê doanh thu theo tháng
    @Query("SELECT FUNCTION('YEAR', p.paidAt) as year, FUNCTION('MONTH', p.paidAt) as month, SUM(p.amount) as total FROM Payment p WHERE p.paidAt BETWEEN :from AND :to AND p.status = 'SUCCESS' GROUP BY FUNCTION('YEAR', p.paidAt), FUNCTION('MONTH', p.paidAt) ORDER BY year, month")
    List<Object[]> getRevenueByMonth(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Thống kê doanh thu theo năm
    @Query("SELECT FUNCTION('YEAR', p.paidAt) as year, SUM(p.amount) as total FROM Payment p WHERE p.paidAt BETWEEN :from AND :to AND p.status = 'SUCCESS' GROUP BY FUNCTION('YEAR', p.paidAt) ORDER BY year")
    List<Object[]> getRevenueByYear(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    Optional<Payment> findByOrder_Id(String orderId);
}
