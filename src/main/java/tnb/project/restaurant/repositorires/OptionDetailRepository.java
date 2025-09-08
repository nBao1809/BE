package tnb.project.restaurant.repositorires;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnb.project.restaurant.entities.OptionDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionDetailRepository extends JpaRepository<OptionDetail,Long> {
    Optional<OptionDetail> findByContentAndOptionTypeId_Id(String content, Long optionTypeId);
    List<OptionDetail> findByOptionType_Id(Long optionTypeId);
}
