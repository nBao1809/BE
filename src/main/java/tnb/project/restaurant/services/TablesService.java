package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
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

    public Tables createTable(Tables table) {
        if (table.getNumber() == null || table.getNumber() <= 0) {
            throw new IllegalArgumentException("Số bàn phải lớn hơn 0");
        }
        if (tablesRepo.findAll().stream().anyMatch(t -> t.getNumber().equals(table.getNumber()))) {
            throw new IllegalArgumentException("Số bàn đã tồn tại");
        }
        if (table.getStatus() == null || table.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái bàn không được để trống");
        }
        return tablesRepo.save(table);
    }

    public Tables updateTable(Long tableId, Tables table) {
        Tables existing = tablesRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với id: " + tableId));
        if (table.getNumber() != null && table.getNumber() > 0) {
            if (tablesRepo.findAll().stream().anyMatch(t -> t.getNumber().equals(table.getNumber()) && !t.getId().equals(tableId))) {
                throw new IllegalArgumentException("Số bàn đã tồn tại");
            }
            existing.setNumber(table.getNumber());
        }
        if (table.getStatus() != null && !table.getStatus().trim().isEmpty()) {
            existing.setStatus(table.getStatus());
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
