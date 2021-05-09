package com.tommy.jpabook.bootjpaapplication.item.domain;

import com.tommy.jpabook.bootjpaapplication.category.Category;
import com.tommy.jpabook.bootjpaapplication.item.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@BatchSize(size = 100) // ToOne 일 경우 전역에다가 BatchSize를 적용할 수 있다.
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

    public Item(Long id, String name, int price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void addStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void removeStockQuantity(int quantity) {
        int resultStockQuantity = this.stockQuantity - quantity;
        if (resultStockQuantity < DEFAULT_STOCK) {
            throw new NotEnoughStockException();
        }
        this.stockQuantity = resultStockQuantity;
    }
}
