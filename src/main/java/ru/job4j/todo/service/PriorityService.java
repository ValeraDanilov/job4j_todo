package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.List;

@Service
@ThreadSafe
public class PriorityService {

    private final PriorityRepository priority;

    public PriorityService(PriorityRepository priority) {
        this.priority = priority;
    }

    public List<Priority> findAll() {
        return this.priority.findAll();
    }
}
