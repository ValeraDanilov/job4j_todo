package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.filter.FilterOptions;
import ru.job4j.todo.repository.ItemRepository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@ThreadSafe
public class ItemService {

    private final ItemRepository item;

    public ItemService(ItemRepository item) {
        this.item = item;
    }

    public void create(Item item) {
        this.item.create(item);
    }

    public List<Item> findAll(FilterOptions filter) {
        List<Item> res = item.findAll();
        return switch (filter) {
            case NEW -> res.stream()
                    .filter(item -> !item.isDone())
                    .collect(Collectors.toList());
            case DONE -> res.stream()
                    .filter(Item::isDone)
                    .collect(Collectors.toList());
            default -> res;
        };
    }

    public Item findById(int id) {
        return this.item.findById(id);
    }

    public void update(Item item) {
        if (this.item.findById(item.getId()) == null) {
            throw new NoSuchElementException("Invalid item id ");
        }
        this.item.update(item);
    }

    public void delete(int id) {
        if (this.item.findById(id) == null) {
            throw new NoSuchElementException("Invalid item id ");
        }
        this.item.delete(id);
    }
}
