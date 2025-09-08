package tnb.project.restaurant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.requests.CreateServiceRequestDTO;
import tnb.project.restaurant.DTO.requests.UpdateStatusDTO;
import tnb.project.restaurant.entities.ServiceRequest;
import tnb.project.restaurant.DTO.responses.ServiceResponseDTO;
import tnb.project.restaurant.services.ServiceRequestService;
import java.util.List;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {
    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @GetMapping
    ResponseEntity<List<ServiceResponseDTO>> getServiceRequests() {
        List<ServiceResponseDTO> serviceRequests = serviceRequestService.getServiceRequestDTOs();
        return ResponseEntity.ok(serviceRequests);
    }

    @GetMapping("/{serviceRequestId}")
    ResponseEntity<ServiceResponseDTO> getServiceRequest(@PathVariable String serviceRequestId) {
        ServiceResponseDTO serviceRequest = serviceRequestService.getServiceRequestDTO(serviceRequestId);
        return ResponseEntity.ok(serviceRequest);
    }

    @PostMapping
    ResponseEntity<ServiceResponseDTO> createServiceRequest(@RequestBody CreateServiceRequestDTO dto) {
        ServiceResponseDTO createdServiceRequest = serviceRequestService.createServiceRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceRequest);
    }

    @PatchMapping("/{serviceRequestId}/handle")
    public ResponseEntity<ServiceResponseDTO> handleServiceRequest(
            @PathVariable Long serviceRequestId,
            @RequestBody UpdateStatusDTO updateStatusDTO) {
        ServiceResponseDTO dto = serviceRequestService.handleRequest(serviceRequestId, updateStatusDTO.getStatus());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<ServiceResponseDTO>> getServiceRequestsBySessionId(@PathVariable String sessionId) {
        List<ServiceResponseDTO> serviceRequests = serviceRequestService.getServiceRequestDTOsBySessionId(sessionId);
        return ResponseEntity.ok(serviceRequests);
    }

}
