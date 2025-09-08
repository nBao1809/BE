package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.entities.ServiceType;
import tnb.project.restaurant.services.ServiceTypeService;
import java.util.List;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping
    ResponseEntity<List<ServiceType>> getServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.getServiceTypes();
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping("/{serviceTypeId}")
    ResponseEntity<ServiceType> getServiceType(@PathVariable Long serviceTypeId) {
        ServiceType serviceType = serviceTypeService.getServiceType(serviceTypeId);
        return ResponseEntity.ok(serviceType);
    }

    @PostMapping
    ResponseEntity<ServiceType> createServiceType(@RequestBody ServiceType serviceType) {
        ServiceType createdServiceType = serviceTypeService.createServiceType(serviceType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceType);
    }

    @PatchMapping("/{serviceTypeId}")
    ResponseEntity<ServiceType> updateServiceType(@PathVariable Long serviceTypeId, @RequestBody ServiceType serviceType) {
        ServiceType updatedServiceType = serviceTypeService.updateServiceType(serviceTypeId, serviceType);
        return ResponseEntity.ok(updatedServiceType);
    }

    @DeleteMapping("/{serviceTypeId}")
    ResponseEntity<Void> deleteServiceType(@PathVariable Long serviceTypeId) {
        serviceTypeService.deleteServiceType(serviceTypeId);
        return ResponseEntity.noContent().build();
    }
}
