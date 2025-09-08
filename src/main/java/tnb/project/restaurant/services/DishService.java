package tnb.project.restaurant.services;

import org.springframework.stereotype.Service;
import tnb.project.restaurant.entities.Dish;
import tnb.project.restaurant.repositorires.DishRepository;
import tnb.project.restaurant.repositorires.CategoryRepository;
import tnb.project.restaurant.repositorires.OptionDetailRepository;
import tnb.project.restaurant.entities.OptionDetail;
import tnb.project.restaurant.entities.Category;
import tnb.project.restaurant.DTO.DishListDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository dishRepo;
    private final CategoryRepository categoryRepo;
    private final OptionDetailRepository optionDetailRepo;
    private final CloudinaryService cloudinaryService;

    public DishService(DishRepository dishRepo, CategoryRepository categoryRepo, OptionDetailRepository optionDetailRepo, CloudinaryService cloudinaryService) {
        this.dishRepo = dishRepo;
        this.categoryRepo = categoryRepo;
        this.optionDetailRepo = optionDetailRepo;
        this.cloudinaryService = cloudinaryService;
    }

    public Dish createDishWithImage(String name, BigDecimal price, Long categoryId, String status, List<Long> optionIds, MultipartFile file) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên món ăn không được để trống");
        }
        if (dishRepo.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Tên món ăn đã tồn tại");
        }
        Dish dish = new Dish();
        dish.setName(name);
        dish.setPrice(price);
        dish.setStatus(status);
        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
            dish.setCategory(category);
        }
        if (optionIds != null) {
            Set<OptionDetail> managedOptions = optionIds.stream()
                .map(id -> optionDetailRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tuỳ chọn: " + id)))
                .collect(Collectors.toSet());
            dish.setOptions(managedOptions);
        }
        handleImageUpload(dish, file);
        return dishRepo.save(dish);
    }

    public Dish updateDishWithImage(Long id, String name, BigDecimal price, Long categoryId, String status, List<Long> optionIds, MultipartFile file) {
        Dish dish = getDish(id);
        if (name != null && !name.trim().isEmpty()) {
            if (dishRepo.findByName(name).filter(d -> !d.getId().equals(id)).isPresent()) {
                throw new IllegalArgumentException("Tên món ăn đã tồn tại");
            }
            dish.setName(name);
        }
        if (price != null) dish.setPrice(price);
        if (status != null) dish.setStatus(status);
        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
            dish.setCategory(category);
        }
        if (optionIds != null) {
            Set<OptionDetail> managedOptions = optionIds.stream()
                .map(optId -> optionDetailRepo.findById(optId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tuỳ chọn: " + optId)))
                .collect(Collectors.toSet());
            dish.setOptions(managedOptions);
        }
        handleImageUpload(dish, file);
        return dishRepo.save(dish);
    }

    public Dish getDish(Long id) {
        return dishRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn với id: " + id));
    }

    private void handleImageUpload(Dish dish, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinaryService.upload(file, "dishes");
                String imageUrl = (String) uploadResult.get("url");
                dish.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }
    }

    public void deleteDish(Long id) {
        Dish dish = getDish(id);
        String imageUrl = dish.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                String[] parts = imageUrl.split("/dishes/");
                if (parts.length == 2) {
                    String afterFolder = parts[1];
                    String publicId = "dishes/" + afterFolder.replaceAll("\\.[^.]+$", "");
                    cloudinaryService.delete(publicId);
                }
            } catch (Exception e) {
                throw new RuntimeException("Xoá ảnh thất bại", e);
            }
        }
        dishRepo.deleteById(id);
    }

    public List<Dish> filterDishes(Long categoryId, String keyword) {
        return dishRepo.findAll().stream()
            .filter(dish -> (categoryId == null || (dish.getCategory() != null && categoryId.equals(dish.getCategory().getId())))
                && (keyword == null || keyword.isBlank() || (dish.getName() != null && dish.getName().toLowerCase().contains(keyword.toLowerCase()))))
            .collect(Collectors.toList());
    }

    public List<DishListDTO> filterDishesDTO(Long categoryId, String keyword) {
        return dishRepo.findAll().stream()
            .filter(dish -> (categoryId == null || (dish.getCategory() != null && categoryId.equals(dish.getCategory().getId())))
                && (keyword == null || keyword.isBlank() || (dish.getName() != null && dish.getName().toLowerCase().contains(keyword.toLowerCase()))))
            .map(dish -> new DishListDTO(
                dish.getId(),
                dish.getName(),
                dish.getPrice(),
                dish.getImageUrl(),
                dish.getStatus(),
                dish.getCategory()
            ))
            .collect(Collectors.toList());
    }

    public Dish updateStatus(Long dishId, String status) {
        Dish dish = dishRepo.findById(dishId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn với id: " + dishId));
        dish.setStatus(status);
        return dishRepo.save(dish);
    }
}
