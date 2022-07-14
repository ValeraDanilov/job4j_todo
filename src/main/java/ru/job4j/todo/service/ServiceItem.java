package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.hbn.RepositoryItem;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ThreadSafe
public class ServiceItem {

    private final RepositoryItem item;

    public ServiceItem(RepositoryItem item) {
        this.item = item;
    }

    public void create(Item item) {
        this.item.create(item);
    }

    public List<Item> findAll(String value) {
        List<Item> res = item.findAll();
        if (value.equals("Новые")) {
            return res.stream().filter(item -> !item.isDone()).collect(Collectors.toList());
        } else if (value.equals("Выполненные")) {
            return res.stream().filter(Item::isDone).collect(Collectors.toList());
        } else {
            return res;
        }
    }

    public Item findById(int id) {
        return this.item.findById(id);
    }

    public void replace(Item item) {
        this.item.replace(item);
    }

    public void delete(int id) {
        this.item.delete(id);
    }
}
