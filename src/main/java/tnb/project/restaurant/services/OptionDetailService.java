package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.OptionDetailDTO;
import tnb.project.restaurant.entities.OptionDetail;
import tnb.project.restaurant.repositorires.OptionDetailRepository;
import tnb.project.restaurant.entities.OptionType;
import tnb.project.restaurant.repositorires.OptionTypeRepository;

import java.util.List;

@Service
public class OptionDetailService {
    private final OptionDetailRepository optionDetailRepo;
    private final OptionTypeRepository optionTypeRepo;

    public OptionDetailService(OptionDetailRepository optionDetailRepo, OptionTypeRepository optionTypeRepo) {
        this.optionDetailRepo = optionDetailRepo;
        this.optionTypeRepo = optionTypeRepo;
    }

    public List<OptionDetail> getOptionDetails() {
        return optionDetailRepo.findAll();
    }

    public OptionDetail getOptionDetail(Long optionDetailId) {
        return optionDetailRepo.findById(optionDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tuỳ chọn chi tiết với id: " + optionDetailId));
    }

    public OptionDetail createOptionDetail(OptionDetailDTO optionDetailDTO) {
        if (optionDetailDTO.getOptionTypeId() == null) {
            throw new IllegalArgumentException("Phải chọn loại tuỳ chọn");
        }
        if (optionDetailDTO.getContent() == null || optionDetailDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung tuỳ chọn không được để trống");
        }
        if (optionDetailRepo.findByContentAndOptionTypeId_Id(optionDetailDTO.getContent(), optionDetailDTO.getOptionTypeId()).isPresent()) {
            throw new IllegalArgumentException("Nội dung tuỳ chọn đã tồn tại trong loại tuỳ chọn này");
        }
        OptionType optionType = optionTypeRepo.findById(optionDetailDTO.getOptionTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại tuỳ chọn"));
        OptionDetail optionDetail = new OptionDetail();
        optionDetail.setOptionType(optionType);
        optionDetail.setContent(optionDetailDTO.getContent());
        return optionDetailRepo.save(optionDetail);
    }

    public OptionDetail updateOptionDetail(Long optionDetailId, OptionDetailDTO optionDetailDTO) {
        OptionDetail existing = optionDetailRepo.findById(optionDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tuỳ chọn chi tiết với id: " + optionDetailId));
        if (optionDetailDTO.getContent() != null && !optionDetailDTO.getContent().trim().isEmpty()) {
            if (optionDetailRepo.findByContentAndOptionTypeId_Id(optionDetailDTO.getContent(), existing.getOptionType().getId())
                .isPresent()) {
                throw new IllegalArgumentException("Nội dung tuỳ chọn đã tồn tại trong loại tuỳ chọn này");
            }
            existing.setContent(optionDetailDTO.getContent());
        }
        if (optionDetailDTO.getOptionTypeId() != null) {
            OptionType optionType = optionTypeRepo.findById(optionDetailDTO.getOptionTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại tuỳ chọn"));
            existing.setOptionType(optionType);
        }
        return optionDetailRepo.save(existing);
    }

    public void deleteOptionDetail(Long optionDetailId) {
        optionDetailRepo.deleteById(optionDetailId);
    }

    public List<OptionDetail> getOptionDetailsByOptionTypeId(Long optionTypeId) {
        return optionDetailRepo.findByOptionType_Id(optionTypeId);
    }
}
