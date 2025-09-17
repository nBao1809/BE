package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.CreateTableRequest;
import tnb.project.restaurant.DTO.UpdateTableRequest;
import tnb.project.restaurant.entities.Tables;
import tnb.project.restaurant.services.TablesService;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TablesController {
    private final TablesService tablesService;

    public TablesController(TablesService tablesService) {
        this.tablesService = tablesService;
    }

    @GetMapping
    ResponseEntity<List<Tables>> getTables() {
        List<Tables> tables = tablesService.getTables();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{tableId}")
    ResponseEntity<Tables> getTable(@PathVariable Long tableId) {
        Tables table = tablesService.getTable(tableId);
        return ResponseEntity.ok(table);
    }

    @PostMapping
    ResponseEntity<Tables> createTable(@RequestBody CreateTableRequest request) {
        Tables createdTable = tablesService.createTable(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
    }

    @PatchMapping("/{tableId}")
    ResponseEntity<Tables> updateTable(@PathVariable Long tableId, @RequestBody UpdateTableRequest request) {
        Tables updatedTable = tablesService.updateTable(tableId,request);
        return ResponseEntity.ok(updatedTable);
    }

    @DeleteMapping("/{tableId}")
    ResponseEntity<Void> deleteTable(@PathVariable Long tableId) {
        tablesService.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }
}
