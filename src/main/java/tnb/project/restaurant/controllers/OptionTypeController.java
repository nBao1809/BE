package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.OptionTypeWithDetailsDTO;
import tnb.project.restaurant.entities.OptionDetail;
import tnb.project.restaurant.entities.OptionType;
import tnb.project.restaurant.services.OptionTypeService;
import java.util.List;

@RestController
@RequestMapping("/api/option-types")
public class OptionTypeController {

    private final OptionTypeService optionTypeService;
    public OptionTypeController(OptionTypeService optionTypeService) {
        this.optionTypeService = optionTypeService;
    }


    @GetMapping
    ResponseEntity<List<OptionTypeWithDetailsDTO>> getOptionTypes() {
        List<OptionTypeWithDetailsDTO> optionTypes = optionTypeService.getOptionTypesWithDetails();
        return ResponseEntity.ok(optionTypes);
    }

    @GetMapping("/{optionTypeId}")
    ResponseEntity<OptionType> getOptionType(@PathVariable Long optionTypeId) {
        OptionType optionType = optionTypeService.getOptionType(optionTypeId);
        return ResponseEntity.ok(optionType);
    }

    @PostMapping
    ResponseEntity<OptionType> createOptionType(@RequestBody OptionType optionType) {
        OptionType createdOptionType = optionTypeService.createOptionType(optionType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOptionType);
    }

    @PatchMapping("/{optionTypeId}")
    ResponseEntity<OptionType> updateOptionType(@PathVariable Long optionTypeId, @RequestBody OptionType optionType) {
        OptionType updatedOptionType = optionTypeService.updateOptionType(optionTypeId, optionType);
        return ResponseEntity.ok(updatedOptionType);
    }

    @DeleteMapping("/{optionTypeId}")
    ResponseEntity<Void> deleteOptionType(@PathVariable Long optionTypeId) {
        optionTypeService.deleteOptionType(optionTypeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{optionTypeId}/option-details")
    ResponseEntity<List<OptionDetail>> getOptionDetailsByOptionType(@PathVariable Long optionTypeId) {
        List<OptionDetail> details = optionTypeService.getOptionTypeWithDetails(optionTypeId).getOptionDetails();
        return ResponseEntity.ok(details);
    }
}
