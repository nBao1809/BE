package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.FeedbackRequestDTO;
import tnb.project.restaurant.DTO.FeedbackResponseDTO;
import tnb.project.restaurant.entities.Feedback;
import tnb.project.restaurant.services.FeedbackService;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    ResponseEntity<?> getFeedbacks(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "size", required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(feedbackService.getFeedbacksPage(page, size));
        } else {
            return ResponseEntity.ok(feedbackService.getFeedbackDTOs());
        }
    }

    @GetMapping("/{feedbackId}")
    ResponseEntity<FeedbackResponseDTO> getFeedback(@PathVariable Long feedbackId) {
        FeedbackResponseDTO feedback = feedbackService.getFeedbackDTO(feedbackId);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping
    ResponseEntity<FeedbackResponseDTO> createFeedback(@RequestBody FeedbackRequestDTO feedback) {
        FeedbackResponseDTO createdFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedback);
    }

//    @PutMapping("/{feedbackId}")
//    ResponseEntity<Feedback> updateFeedback(@PathVariable Long feedbackId, @RequestBody Feedback feedback) {
//        Feedback updatedFeedback = feedbackService.updateFeedback(feedbackId, feedback);
//        return ResponseEntity.ok(updatedFeedback);
//    }
//
//    @DeleteMapping("/{feedbackId}")
//    ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
//        feedbackService.deleteFeedback(feedbackId);
//        return ResponseEntity.noContent().build();
//    }
}
