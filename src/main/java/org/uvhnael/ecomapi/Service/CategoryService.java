package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.Category;
import org.uvhnael.ecomapi.Repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.getAll();
    }

    public Category getById(int id) {
        return categoryRepository.getById(id);
    }

    public Category getByProductId(int productId) {
        return categoryRepository.getByProductId(productId);
    }

    public Boolean isCategoryExists(int id) {
        return categoryRepository.isCategoryExists(id);
    }

    public Boolean create(Category category) {
        return categoryRepository.addCategory(category);
    }

    public Boolean update(Category category) {
        return categoryRepository.updateCategory(category);
    }

    public Boolean delete(int id) {
        return categoryRepository.deleteCategory(id);
    }
}
