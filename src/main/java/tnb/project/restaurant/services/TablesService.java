package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.CreateTableRequest;
import tnb.project.restaurant.DTO.UpdateTableRequest;
import tnb.project.restaurant.entities.Tables;
import tnb.project.restaurant.repositorires.TablesRepository;

import java.util.List;

@Service
public class TablesService {
    private final TablesRepository tablesRepo;

    public TablesService(TablesRepository tablesRepo) {
        this.tablesRepo = tablesRepo;
    }

    public List<Tables> getTables() {
        return tablesRepo.findAll();
    }

    public Tables getTable(Long tableId) {
        return tablesRepo.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));
    }

    public Tables createTable(CreateTableRequest request) {
        Integer number = request.getNumber();
        if (number == null || number <= 0) {
            throw new IllegalArgumentException("Số bàn phải lớn hơn 0");
        }
        if (tablesRepo.findAll().stream().anyMatch(t -> t.getNumber().equals(number))) {
            throw new IllegalArgumentException("Số bàn đã tồn tại");
        }
        Tables table = new Tables();
        table.setNumber(number);
        table.setStatus("AVAILABLE"); // BE tự tạo status mặc định
        return tablesRepo.save(table);
    }

    public Tables updateTable(Long tableId, UpdateTableRequest request) {
        Tables existing = tablesRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với id: " + tableId));
        Integer number = request.getNumber();
        String status = request.getStatus();
        if (number != null && number > 0) {
            if (tablesRepo.findAll().stream().anyMatch(t -> t.getNumber().equals(number) && !t.getId().equals(tableId))) {
                throw new IllegalArgumentException("Số bàn đã tồn tại");
            }
            existing.setNumber(number);
        }
        if (status != null && !status.trim().isEmpty()) {
            existing.setStatus(status);
        }
        return tablesRepo.save(existing);
    }

    public Tables updateStatus(Long tableId, String status) {
        Tables table = tablesRepo.findById(tableId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với id: " + tableId));
        table.setStatus(status);
        return tablesRepo.save(table);
    }

    public void deleteTable(Long tableId) {
        if (!tablesRepo.existsById(tableId)) {
            throw new IllegalArgumentException("Không tìm thấy bàn với id: " + tableId);
        }
        tablesRepo.deleteById(tableId);
    }
}
