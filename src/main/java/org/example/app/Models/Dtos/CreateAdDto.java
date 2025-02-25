package org.example.app.Models.Dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class CreateAdDto {
    private String title;
    private float price;
    private String description;
    private String condition;
    private String username;
    private UUID categoryId;
    private List<MultipartFile> images;


    public CreateAdDto(String title, float price, String description, String condition, String username, UUID categoryId, List<MultipartFile> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.condition = condition;
        this.username = username;
        this.categoryId = categoryId;
        this.images = images;
    }


    @Override
    public String toString() {
        return "CreateAdDto{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                ", username='" + username + '\'' +
                ", images=" + images +
                '}';
    }
}
