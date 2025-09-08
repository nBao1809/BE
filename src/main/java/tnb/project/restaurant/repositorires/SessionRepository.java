package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.Session;
@Repository
public interface SessionRepository extends JpaRepository<Session,String> {
}
