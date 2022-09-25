package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.filter.FilterOptions;
import ru.job4j.todo.repository.ItemRepository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
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

    public List<Item> findAllByFilter(FilterOptions filter) {
        Predicate<Item> condition = switch (filter) {
            case NEW -> item -> !item.isDone();
            case DONE -> Item::isDone;
            default -> item -> true;
        };
        return this.item.findAll().stream().filter(condition).distinct().toList();
    }

    public Item findById(int id) {
        return this.item.findById(id);
    }

    public void update(Item item) {
        this.item.update(item);
    }

    public void delete(int id) {
        this.item.delete(id);
    }
}
