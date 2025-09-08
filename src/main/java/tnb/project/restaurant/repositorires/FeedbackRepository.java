package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.Feedback;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    Optional<Feedback> findBySession_Id(String sessionId);
}
