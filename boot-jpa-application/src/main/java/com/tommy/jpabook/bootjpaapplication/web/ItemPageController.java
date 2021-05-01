package com.tommy.jpabook.bootjpaapplication.web;

import com.tommy.jpabook.bootjpaapplication.item.domain.Book;
import com.tommy.jpabook.bootjpaapplication.item.domain.Item;
import com.tommy.jpabook.bootjpaapplication.item.dto.BookRequest;
import com.tommy.jpabook.bootjpaapplication.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
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

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        Book findItem = (Book) itemService.findById(itemId);

        Long id = findItem.getId();
        String name = findItem.getName();
        int price = findItem.getPrice();
        int stockQuantity = findItem.getStockQuantity();
        String author = findItem.getAuthor();
        String isbn = findItem.getIsbn();

        BookRequest bookRequest = new BookRequest(id, name, price, stockQuantity, author, isbn);

        model.addAttribute("itemRequest", bookRequest);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId,
                             @ModelAttribute("itemRequest") BookRequest bookRequest) {

        String name = bookRequest.getName();
        int price = bookRequest.getPrice();
        int stockQuantity = bookRequest.getStockQuantity();
        String author = bookRequest.getAuthor();
        String isbn = bookRequest.getIsbn();

        Book book = new Book(itemId, name, price, stockQuantity, author, isbn);

        itemService.saveItem(book);
        return "redirect:/items";
    }
}
