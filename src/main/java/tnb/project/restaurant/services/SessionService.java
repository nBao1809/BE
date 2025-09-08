package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.entities.Session;
import tnb.project.restaurant.DTO.SessionDTO;
import tnb.project.restaurant.mapper.SessionMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import tnb.project.restaurant.repositorires.SessionRepository;
import tnb.project.restaurant.repositorires.TablesRepository;
import tnb.project.restaurant.repositorires.CustomerRepository;
import tnb.project.restaurant.entities.Tables;
import tnb.project.restaurant.entities.Customer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class SessionService {
    private final SessionRepository sessionRepo;
    private final TablesRepository tablesRepo;
    private final CustomerRepository customerRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerService customerService;

    public SessionService(SessionRepository sessionRepo, TablesRepository tablesRepo, CustomerRepository customerRepo, SimpMessagingTemplate messagingTemplate, CustomerService customerService) {
        this.sessionRepo = sessionRepo;
        this.tablesRepo = tablesRepo;
        this.customerRepo = customerRepo;
        this.messagingTemplate = messagingTemplate;
        this.customerService = customerService;
    }

    public List<Session> getSessions() {
        return sessionRepo.findAll();
    }

    public Session getSession(String sessionId) {
        return sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn với id: " + sessionId));
    }

    public List<SessionDTO> getSessionDTOs() {
        return sessionRepo.findAll().stream()
                .map(SessionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SessionDTO getSessionDTO(String sessionId) {
        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn với id: " + sessionId));
        return SessionMapper.toDTO(session);
    }

    public Session createSession(Long tableId) {
        // Kiểm tra bảng
        if (tableId == null) {
            throw new IllegalArgumentException("Phải chọn bàn");
        }
        Session session = new Session();
        Tables table = tablesRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn"));
        session.setTable(table);
        session.setStartTime(LocalDateTime.now());
        session.setStatus("OPEN");
        Session savedSession = sessionRepo.save(session);
        table.setStatus("OCCUPIED");
        tablesRepo.save(table);
        // Gửi thông báo WS cho thu ngân
        messagingTemplate.convertAndSend("/topic/cashier/session", SessionMapper.toDTO(savedSession));
        return savedSession;
    }

    public Session updateSession(String sessionId, Session session) {
        Session existing = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn với id: " + sessionId));
        // Kiểm tra bảng
        if (session.getTable() != null && session.getTable().getId() != null) {
            Tables table = tablesRepo.findById(session.getTable().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn"));
            existing.setTable(table);
        }
        // Kiểm tra khách hàng
        if (session.getCustomer() != null && session.getCustomer().getId() != null) {
            Customer customer = customerRepo.findById(session.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));
            existing.setCustomer(customer);
        }
        if (session.getEndTime() != null) {
            existing.setEndTime(session.getEndTime());
        }
        if (session.getStatus() != null) {
            existing.setStatus(session.getStatus());
        }
        return sessionRepo.save(existing);
    }

    public Session endSession(String sessionId, String customerId) {
        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn với id: " + sessionId));
        if (customerId != null && !customerId.isEmpty()) {
            Customer customer = customerService.getCustomer(customerId);
            session.setCustomer(customer);
        }
        session.setStatus("ENDED");
        session.setEndTime(LocalDateTime.now());
        Session endedSession = sessionRepo.save(session);
        Tables table = session.getTable();
        if (table != null) {
            table.setStatus("AVAILABLE");
            tablesRepo.save(table);
        }
        // Gửi thông báo WS cho thu ngân
        messagingTemplate.convertAndSend("/topic/cashier/session", SessionMapper.toDTO(endedSession));
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, SessionMapper.toDTO(endedSession));
        return endedSession;
    }


    public void deleteSession(String sessionId) {
        Session existing = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn với id: " + sessionId));
        messagingTemplate.convertAndSend("/topic/cashier/session", SessionMapper.toDTO(existing));
        messagingTemplate.convertAndSend("/topic/session/" + existing.getId(), SessionMapper.toDTO(existing));
        sessionRepo.deleteById(sessionId);
    }
}
