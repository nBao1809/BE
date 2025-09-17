package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.entities.ServiceRequest;
import java.util.List;
import tnb.project.restaurant.repositorires.ServiceRequestRepository;
import tnb.project.restaurant.repositorires.SessionRepository;
import tnb.project.restaurant.repositorires.ServiceTypeRepository;
import tnb.project.restaurant.entities.Session;
import tnb.project.restaurant.entities.ServiceType;

import java.time.LocalDateTime;
import tnb.project.restaurant.DTO.responses.ServiceResponseDTO;
import tnb.project.restaurant.mapper.ServiceRequestMapper;
import tnb.project.restaurant.DTO.requests.CreateServiceRequestDTO;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final SessionRepository sessionRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository, SessionRepository sessionRepository, ServiceTypeRepository serviceTypeRepository, SimpMessagingTemplate messagingTemplate) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.sessionRepository = sessionRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<ServiceRequest> getServiceRequests() {
        return serviceRequestRepository.findAll().stream().filter(s -> !s.getStatus().equals("COMPLETED")).collect(Collectors.toList());
    }

    public ServiceRequest getServiceRequest(String serviceRequestId) {
        return serviceRequestRepository.findById(Long.valueOf(serviceRequestId))
                .orElseThrow(() -> new RuntimeException("ServiceRequest not found"));
    }

    public ServiceResponseDTO createServiceRequest(CreateServiceRequestDTO dto) {
        // Kiểm tra session
        if (dto.getSessionId() == null) {
            throw new IllegalArgumentException("Phải chọn phiên ăn");
        }
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn"));
        // Kiểm tra serviceType
        if (dto.getServiceTypeId() == null) {
            throw new IllegalArgumentException("Phải chọn loại dịch vụ");
        }
        ServiceType serviceType = serviceTypeRepository.findById(dto.getServiceTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ"));
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setSession(session);
        serviceRequest.setServiceType(serviceType);
        serviceRequest.setCreated_at(LocalDateTime.now());
        serviceRequest.setStatus("PENDING");
        ServiceRequest saved = serviceRequestRepository.save(serviceRequest);
        // Gửi thông báo qua WebSocket khi tạo yêu cầu dịch vụ
        ServiceResponseDTO responseDTO = ServiceRequestMapper.toDTO(saved);
        messagingTemplate.convertAndSend("/topic/cashier/service-request",  responseDTO);
        // Trả về DTO đã map
        return responseDTO;
    }

//    public ServiceRequest updateServiceRequest(String serviceRequestId, ServiceRequest serviceRequest) {
//        ServiceRequest existing = serviceRequestRepository.findById(Long.valueOf(serviceRequestId))
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu dịch vụ với id: " + serviceRequestId));
//        // Kiểm tra session
//        if (serviceRequest.getSession() != null && serviceRequest.getSession().getId() != null) {
//            Session session = sessionRepository.findById(serviceRequest.getSession().getId())
//                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiên ăn"));
//            existing.setSession(session);
//        }
//        // Kiểm tra serviceType
//        if (serviceRequest.getServiceType() != null && serviceRequest.getServiceType().getId() != null) {
//            ServiceType serviceType = serviceTypeRepository.findById(serviceRequest.getServiceType().getId())
//                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ"));
//            existing.setServiceType(serviceType);
//        }
//        // Không cập nhật created_at khi update
//        if (serviceRequest.getHandleTme() != null) {
//            existing.setHandleTme(serviceRequest.getHandleTme());
//        }
//        if (serviceRequest.getStatus() != null) {
//            existing.setStatus(serviceRequest.getStatus());
//        }
//        return serviceRequestRepository.save(existing);
//    }

    public void deleteServiceRequest(String serviceRequestId) {
        if (!serviceRequestRepository.existsById(Long.valueOf(serviceRequestId))) {
            throw new IllegalArgumentException("Không tìm thấy yêu cầu dịch vụ với id: " + serviceRequestId);
        }
        serviceRequestRepository.deleteById(Long.valueOf(serviceRequestId));
    }

    public List<ServiceResponseDTO> getServiceRequestDTOs() {
        return serviceRequestRepository.findAll().stream().filter(s -> !s.getStatus().equals("COMPLETED"))
            .map(ServiceRequestMapper::toDTO)
            .collect(Collectors.toList());
    }

    public ServiceResponseDTO getServiceRequestDTO(String serviceRequestId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(Long.valueOf(serviceRequestId))
                .orElseThrow(() -> new RuntimeException("ServiceRequest not found"));
        return ServiceRequestMapper.toDTO(serviceRequest);
    }

    public ServiceResponseDTO handleRequest(Long serviceRequestId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu dịch vụ với id: " + serviceRequestId));
        serviceRequest.setStatus("COMPLETED");
        serviceRequest.setHandleTime(LocalDateTime.now());
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        // Gửi thông báo qua WebSocket khi xử lý yêu cầu dịch vụ
        Session session = updatedRequest.getSession();
        ServiceResponseDTO dto = ServiceRequestMapper.toDTO(updatedRequest);
        messagingTemplate.convertAndSend("/topic/session/" + session.getId() + "/service-request", dto);
        return dto;
    }

    public List<ServiceResponseDTO> getServiceRequestDTOsBySessionId(String sessionId) {
        return serviceRequestRepository.findBySession_Id(sessionId).stream()
            .map(ServiceRequestMapper::toDTO)
            .collect(Collectors.toList());
    }

}
