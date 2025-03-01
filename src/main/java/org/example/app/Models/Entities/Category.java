package org.example.app.Models.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@ToString
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> childCategories;

    private String name;


    public Category() {
    }

    public Category(Category parent, String name) {
        this.parent = parent;
        this.name = name;
    }
}
