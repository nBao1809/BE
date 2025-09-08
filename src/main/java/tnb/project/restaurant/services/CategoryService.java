package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.responses.CategoryWithDishesDTO;
import tnb.project.restaurant.entities.Category;
import tnb.project.restaurant.entities.Dish;
import tnb.project.restaurant.repositorires.CategoryRepository;
import tnb.project.restaurant.repositorires.DishRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;
    private final DishRepository dishRepo;

    public CategoryService(CategoryRepository categoryRepo, DishRepository dishRepo) {
        this.categoryRepo = categoryRepo;
        this.dishRepo = dishRepo;
    }

    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategory(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với id: " + categoryId));
    }

    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        if (categoryRepo.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        return categoryRepo.save(category);
    }

    public Category updateCategory(Long categoryId, Category category) {
        Category existingCategory = getCategory(categoryId);
        if (category.getName() != null && !category.getName().trim().isEmpty()) {
            if (categoryRepo.findByName(category.getName()).filter(c -> !c.getId().equals(categoryId)).isPresent()) {
                throw new IllegalArgumentException("Tên danh mục đã tồn tại");
            }
            existingCategory.setName(category.getName());
        }
        return categoryRepo.save(existingCategory);
    }

    public void deleteCategory(Long categoryId) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new IllegalArgumentException("Không tìm thấy danh mục với id: " + categoryId);
        }
        categoryRepo.deleteById(categoryId);
    }

    public List<CategoryWithDishesDTO> getCategoriesWithDishes() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream().map(category -> {
            CategoryWithDishesDTO dto = new CategoryWithDishesDTO();
            dto.id = category.getId();
            dto.name = category.getName();
            List<Dish> dishes = dishRepo.findAllByCategory(category);
            dto.dishes = dishes.stream().map(dish -> {
                CategoryWithDishesDTO.DishShortDTO d = new CategoryWithDishesDTO.DishShortDTO();
                d.id = dish.getId();
                d.name = dish.getName();
                d.price = dish.getPrice();
                d.imageUrl = dish.getImageUrl();
                d.status= dish.getStatus();
                return d;
            }).collect(Collectors.toList());
            return dto;
        }).collect(Collectors.toList());
    }
}
