package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.util.SessionWrapper;

import java.util.List;


@Repository
public class PriorityRepository {

    private static final String SELECT_ALL_QUERY = "from Priority i order by i.id";
    private final SessionFactory factory;

    public PriorityRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public List<Priority> findAll() {
        return SessionWrapper.wrap(session ->
                session.createQuery(SELECT_ALL_QUERY, Priority.class)
                        .list(), factory);
    }
}
