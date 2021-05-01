package com.tommy.jpabook.bootjpaapplication.item.service;

import com.tommy.jpabook.bootjpaapplication.item.domain.Book;
import com.tommy.jpabook.bootjpaapplication.item.domain.Item;
import com.tommy.jpabook.bootjpaapplication.item.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public Long saveItem(Item item) {
        return itemRepository.save(item);
    }

    // 더티체킹을 이용한 update가 더 권장된다.
    public void updateItem(Long itemId, Book bookParam) {
        Item findItem = itemRepository.findById(itemId);
        findItem.updateName(bookParam.getName());
    }

    @Transactional(readOnly = true)
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId);
    }
}
