package com.tommy.jpabook.bootjpaapplication.item;

import com.tommy.jpabook.bootjpaapplication.category.Category;
import com.tommy.jpabook.bootjpaapplication.item.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Item {

    private static final int DEFAULT_STOCK = 0;

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    public void addStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStockQuantity(int quantity) {
        int resultStockQuantity = this.stockQuantity - quantity;
        if (resultStockQuantity < DEFAULT_STOCK) {
            throw new NotEnoughStockException();
        }
        this.stockQuantity = resultStockQuantity;
    }
}
