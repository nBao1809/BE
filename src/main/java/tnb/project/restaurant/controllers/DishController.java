package tnb.project.restaurant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tnb.project.restaurant.DTO.DishListDTO;
import tnb.project.restaurant.DTO.requests.UpdateStatusDTO;
import tnb.project.restaurant.entities.Dish;
import tnb.project.restaurant.services.DishService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<DishListDTO>> getDishes(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                      @RequestParam(value = "q", required = false) String keyword) {
        List<DishListDTO> dishes = dishService.filterDishesDTO(categoryId, keyword);
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDish(@PathVariable Long id) {
        Dish dish = dishService.getDish(id);
        if (dish == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dish);
    }

    @PostMapping
    public ResponseEntity<Dish> createDish(
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "optionIds", required = false) List<Long> optionIds,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        Dish createdDish = dishService.createDishWithImage(name, price, categoryId, status, optionIds, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Dish> updateDish(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "optionIds", required = false) List<Long> optionIds,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        Dish updatedDish = dishService.updateDishWithImage(id, name, price, categoryId, status, optionIds, file);
        return ResponseEntity.ok(updatedDish);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Dish> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO dto) {
        Dish updated = dishService.updateStatus(id, dto.getStatus());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
