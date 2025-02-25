package org.example.app.Models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.app.Models.Entities.Category;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDto {
    private UUID id;

    private UUID parentId;
    private String parentName;

    private String name;
    private List<CategoryDto> childCategories;

    public CategoryDto(UUID id, UUID parentId, String parentName, String name) {
        this.id = id;
        this.parentId = parentId;
        this.parentName = parentName;
        this.name = name;
    }
}
