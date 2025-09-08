package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.Tables;
@Repository
public interface TablesRepository extends JpaRepository<Tables,Long> {

}
