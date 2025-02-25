package org.example.app.Services;

import org.example.app.Daos.CategoryDao;
import org.example.app.Models.Dtos.CategoryDto;
import org.example.app.Models.Entities.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryDao.getTopLevelCategories();
        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (Category category : categories) {
            categoryDtos.add(toCategoryDto(category));
        }

        return categoryDtos;
    }


    public CategoryDto toCategoryDto(Category category) {
        List<Category> childCategories = category.getChildCategories();

        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getParent() != null ? category.getParent().getName() : null,
                category.getName()
        );
        List<CategoryDto> childrenCategoriesDto = new ArrayList<>();

        // recursively build category hierarchy
        for (Category childCategory : childCategories) {
            childrenCategoriesDto.add(toCategoryDto(childCategory));
        }

        categoryDto.setChildCategories(childrenCategoriesDto);

        return categoryDto;
    }
}
