package org.example.app.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.app.Models.Entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDao.class);

    @PersistenceContext
    private EntityManager entityManager;


    public List<Category> getTopLevelCategories() {
        List<Category> categories = entityManager.createNativeQuery(
                "SELECT * FROM category WHERE parent_id IS NULL", Category.class
        ).getResultList();

        return categories;
    }


    public Category getCategory(UUID categoryId) {
        return entityManager.find(Category.class, categoryId);
    }
}
