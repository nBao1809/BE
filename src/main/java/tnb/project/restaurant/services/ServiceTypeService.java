package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.entities.ServiceType;
import tnb.project.restaurant.repositorires.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> getServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    public ServiceType getServiceType(Long serviceTypeId) {
        return serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ với id: " + serviceTypeId));
    }

    public ServiceType createServiceType(ServiceType serviceType) {
        if (serviceType.getTypeName() == null || serviceType.getTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại dịch vụ không được để trống");
        }
        if (serviceTypeRepository.findAll().stream().anyMatch(st -> st.getTypeName().equalsIgnoreCase(serviceType.getTypeName()))) {
            throw new IllegalArgumentException("Tên loại dịch vụ đã tồn tại");
        }
        return serviceTypeRepository.save(serviceType);
    }

    public ServiceType updateServiceType(Long serviceTypeId, ServiceType serviceType) {
        ServiceType existing = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ với id: " + serviceTypeId));
        if (serviceType.getTypeName() != null && !serviceType.getTypeName().trim().isEmpty()) {
            if (serviceTypeRepository.findAll().stream().anyMatch(st -> st.getTypeName().equalsIgnoreCase(serviceType.getTypeName()) && !st.getId().equals(serviceTypeId))) {
                throw new IllegalArgumentException("Tên loại dịch vụ đã tồn tại");
            }
            existing.setTypeName(serviceType.getTypeName());
        }
        if (serviceType.getDescription() != null) {
            existing.setDescription(serviceType.getDescription());
        }
        return serviceTypeRepository.save(existing);
    }

    public void deleteServiceType(Long serviceTypeId) {
        if (!serviceTypeRepository.existsById(serviceTypeId)) {
            throw new IllegalArgumentException("Không tìm thấy loại dịch vụ với id: " + serviceTypeId);
        }
        serviceTypeRepository.deleteById(serviceTypeId);
    }
}
