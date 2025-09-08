package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.entities.OptionType;
import tnb.project.restaurant.repositorires.OptionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import tnb.project.restaurant.DTO.OptionTypeWithDetailsDTO;
import tnb.project.restaurant.entities.OptionDetail;
import tnb.project.restaurant.services.OptionDetailService;

import java.util.List;

@Service
public class OptionTypeService {
    private final OptionTypeRepository optionTypeRepository;
    private final OptionDetailService optionDetailService;

    public OptionTypeService(OptionTypeRepository optionTypeRepository, OptionDetailService optionDetailService) {
        this.optionTypeRepository = optionTypeRepository;
        this.optionDetailService = optionDetailService;
    }

    public List<OptionType> getOptionTypes() {
        return optionTypeRepository.findAll();
    }

    public OptionType getOptionType(Long optionTypeId) {
        return optionTypeRepository.findById(optionTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại tuỳ chọn với id: " + optionTypeId));
    }

    public OptionType createOptionType(OptionType optionType) {
        if (optionType.getContent() == null || optionType.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại tuỳ chọn không được để trống");
        }
        if (optionTypeRepository.findByContent(optionType.getContent()).isPresent()) {
            throw new IllegalArgumentException("Tên loại tuỳ chọn đã tồn tại");
        }
        return optionTypeRepository.save(optionType);
    }

    public OptionType updateOptionType(Long optionTypeId, OptionType optionType) {
        OptionType existing = optionTypeRepository.findById(optionTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại tuỳ chọn với id: " + optionTypeId));
        if (optionType.getContent() != null && !optionType.getContent().trim().isEmpty()) {
            if (optionTypeRepository.findByContent(optionType.getContent())
                    .filter(ot -> !ot.getId().equals(optionTypeId)).isPresent()) {
                throw new IllegalArgumentException("Tên loại tuỳ chọn đã tồn tại");
            }
            existing.setContent(optionType.getContent());
        }
        return optionTypeRepository.save(existing);
    }

    public void deleteOptionType(Long optionTypeId) {
        if (!optionTypeRepository.existsById(optionTypeId)) {
            throw new IllegalArgumentException("Không tìm thấy loại tuỳ chọn với id: " + optionTypeId);
        }
        optionTypeRepository.deleteById(optionTypeId);
    }

    public List<OptionTypeWithDetailsDTO> getOptionTypesWithDetails() {
        List<OptionType> optionTypes = optionTypeRepository.findAll();
        return optionTypes.stream().map(optionType -> {
            OptionTypeWithDetailsDTO dto = new OptionTypeWithDetailsDTO();
            dto.setId(optionType.getId());
            dto.setContent(optionType.getContent());
            List<OptionDetail> details = optionDetailService.getOptionDetailsByOptionTypeId(optionType.getId());
            dto.setOptionDetails(details);
            return dto;
        }).toList();
    }

    public OptionTypeWithDetailsDTO getOptionTypeWithDetails(Long optionTypeId) {
        OptionType optionType = getOptionType(optionTypeId);
        OptionTypeWithDetailsDTO dto = new OptionTypeWithDetailsDTO();
        dto.setId(optionType.getId());
        dto.setContent(optionType.getContent());
        List<OptionDetail> details = optionDetailService.getOptionDetailsByOptionTypeId(optionTypeId);
        dto.setOptionDetails(details);
        return dto;
    }
}
