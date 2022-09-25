package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.List;

@Service
@ThreadSafe
public class CategoryService {

    private final CategoryRepository service;

    public CategoryService(CategoryRepository service) {
        this.service = service;
    }

    public List<Category> findAll() {
        return this.service.findAll();
    }

    public Category findById(int id) {
        return this.service.findById(id);
    }
}
