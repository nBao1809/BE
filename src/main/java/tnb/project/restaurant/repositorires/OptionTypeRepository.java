package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.OptionType;

import java.util.Optional;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType,Long> {
    Optional<OptionType> findByContent(String content);
}
