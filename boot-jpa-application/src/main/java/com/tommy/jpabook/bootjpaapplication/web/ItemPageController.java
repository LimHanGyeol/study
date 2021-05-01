package com.tommy.jpabook.bootjpaapplication.web;

import com.tommy.jpabook.bootjpaapplication.item.domain.Book;
import com.tommy.jpabook.bootjpaapplication.item.domain.Item;
import com.tommy.jpabook.bootjpaapplication.item.dto.BookRequest;
import com.tommy.jpabook.bootjpaapplication.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemPageController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("itemRequest", new BookRequest());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookRequest bookRequest) {
        String name = bookRequest.getName();
        int price = bookRequest.getPrice();
        int stockQuantity = bookRequest.getStockQuantity();
        String author = bookRequest.getAuthor();
        String isbn = bookRequest.getIsbn();

        Book book = new Book(name, price, stockQuantity, author, isbn);

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }
}
