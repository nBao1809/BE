package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.Category;
import tnb.project.restaurant.entities.Dish;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish,Long> {
    Optional<Dish> findByName(String name);
    List<Dish> findAllByCategory(Category category);
}
