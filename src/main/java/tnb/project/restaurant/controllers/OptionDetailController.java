package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.entities.OptionDetail;
import tnb.project.restaurant.services.OptionDetailService;
import java.util.List;

@RestController
@RequestMapping("/api/option-details")
public class OptionDetailController {
    private final OptionDetailService optionDetailService;

    public OptionDetailController(OptionDetailService optionDetailService) {
        this.optionDetailService = optionDetailService;
    }

    @GetMapping
    ResponseEntity<List<OptionDetail>> getOptionDetails() {
        List<OptionDetail> optionDetails = optionDetailService.getOptionDetails();
        return ResponseEntity.ok(optionDetails);
    }

    @GetMapping("/{optionDetailId}")
    ResponseEntity<OptionDetail> getOptionDetail(@PathVariable Long optionDetailId) {
        OptionDetail optionDetail = optionDetailService.getOptionDetail(optionDetailId);
        return ResponseEntity.ok(optionDetail);
    }

    @PostMapping
    ResponseEntity<OptionDetail> createOptionDetail(@RequestBody OptionDetail optionDetail) {
        OptionDetail createdOptionDetail = optionDetailService.createOptionDetail(optionDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOptionDetail);
    }

    @PatchMapping("/{optionDetailId}")
    ResponseEntity<OptionDetail> updateOptionDetail(@PathVariable Long optionDetailId, @RequestBody OptionDetail optionDetail) {
        OptionDetail updatedOptionDetail = optionDetailService.updateOptionDetail(optionDetailId, optionDetail);
        return ResponseEntity.ok(updatedOptionDetail);
    }

    @DeleteMapping("/{optionDetailId}")
    ResponseEntity<Void> deleteOptionDetail(@PathVariable Long optionDetailId) {
        optionDetailService.deleteOptionDetail(optionDetailId);
        return ResponseEntity.noContent().build();
    }
}
