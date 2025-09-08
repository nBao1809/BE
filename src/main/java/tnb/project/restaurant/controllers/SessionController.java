package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.entities.Session;
import tnb.project.restaurant.services.SessionService;
import tnb.project.restaurant.DTO.SessionDTO;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    ResponseEntity<List<SessionDTO>> getSessions() {
        List<SessionDTO> sessions = sessionService.getSessionDTOs();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{sessionId}")
    ResponseEntity<SessionDTO> getSession(@PathVariable String sessionId) {
        SessionDTO session = sessionService.getSessionDTO(sessionId);
        return ResponseEntity.ok(session);
    }

    public static class CreateSessionRequest {
        public Long tableId;
    }

    @PostMapping
    ResponseEntity<Session> createSession(@RequestBody CreateSessionRequest request) {
        Session createdSession = sessionService.createSession(request.tableId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    @PatchMapping("/{sessionId}")
    ResponseEntity<Session> updateSession(@PathVariable String sessionId, @RequestBody Session session) {
        Session updatedSession = sessionService.updateSession(sessionId, session);
        return ResponseEntity.ok(updatedSession);
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<Session> endSession(@PathVariable String sessionId) {
        Session ended = sessionService.endSession(sessionId,null);
        return ResponseEntity.ok(ended);
    }

    ResponseEntity<Void> deleteSession(@PathVariable String sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
