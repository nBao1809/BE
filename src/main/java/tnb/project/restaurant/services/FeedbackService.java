package tnb.project.restaurant.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.FeedbackRequestDTO;
import tnb.project.restaurant.DTO.FeedbackResponseDTO;
import tnb.project.restaurant.DTO.PageResponse;
import tnb.project.restaurant.entities.Feedback;
import tnb.project.restaurant.entities.Session;
import tnb.project.restaurant.mapper.FeedbackMapper;
import tnb.project.restaurant.repositorires.FeedbackRepository;
import tnb.project.restaurant.repositorires.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepo;
    private final SessionRepository sessionRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, SessionRepository sessionRepository) {
        this.feedbackRepo = feedbackRepository;
        this.sessionRepository = sessionRepository;
    }


    public List<Feedback> getFeedbacks() {
        return feedbackRepo.findAll();
    }

    public List<FeedbackResponseDTO> getFeedbackDTOs() {
        return feedbackRepo.findAll().stream()
            .map(FeedbackMapper::toDTO)
            .toList();
    }


    public Feedback getFeedback(Long feedbackId) {
        return feedbackRepo.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Kh��ng tìm thấy phản hồi với id: " + feedbackId));
    }

    public FeedbackResponseDTO getFeedbackDTO(Long feedbackId) {
        Feedback feedback = feedbackRepo.findById(feedbackId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phản hồi với id: " + feedbackId));
        return FeedbackMapper.toDTO(feedback);
    }

    public FeedbackResponseDTO createFeedback(FeedbackRequestDTO request) {
        if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
            throw new IllegalArgumentException("Phản hồi phải gắn với một phiên ăn (session)");
        }
        Session session = sessionRepository.findById(request.getSessionId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn với id: " + request.getSessionId()));
        if (feedbackRepo.findBySession_Id(session.getId()).isPresent()) {
            throw new IllegalArgumentException("Đã tồn tại phản hồi cho phiên ăn này");
        }
        Feedback feedback = new Feedback();
        feedback.setSession(session);
        feedback.setRating(request.getRating());
        feedback.setContent(request.getContent());
        feedback.setCreatedAt(LocalDateTime.now());
        Feedback saved = feedbackRepo.save(feedback);
        return FeedbackMapper.toDTO(saved);
    }

    public void deleteFeedback(Long feedbackId) {
        if (!feedbackRepo.existsById(feedbackId)) {
            throw new IllegalArgumentException("Không tìm thấy phản hồi với id: " + feedbackId);
        }
        feedbackRepo.deleteById(feedbackId);
    }

    public PageResponse<FeedbackResponseDTO> getFeedbacksPage(Integer page, Integer size) {
        if (page != null && size != null) {
            Page<Feedback> feedbackPage = feedbackRepo.findAll(PageRequest.of(page, size));
            List<FeedbackResponseDTO> content = feedbackPage.getContent().stream().map(FeedbackMapper::toDTO).toList();
            return new PageResponse<>(content, feedbackPage.getNumber(), feedbackPage.getSize(), feedbackPage.getTotalElements(), feedbackPage.getTotalPages());
        } else {
            List<FeedbackResponseDTO> content = feedbackRepo.findAll().stream().map(FeedbackMapper::toDTO).toList();
            return new PageResponse<>(content, 0, content.size(), content.size(), 1);
        }
    }
}
