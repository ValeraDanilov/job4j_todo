package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.util.SessionWrapper;

import java.util.List;

@Repository
public class CategoryRepository {

    private static final String SELECT_ALL_QUERY = "from Category i order by i.id";
    private static final String SELECT_BY_ID_QUERY = "from Category i where i.id = :Id";
    private final SessionFactory factory;

    public CategoryRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public List<Category> findAll() {
        return SessionWrapper.wrap(session ->
                session.createQuery(SELECT_ALL_QUERY, Category.class)
                        .list(), factory);
    }

    public Category findById(int id) {
        return (Category) SessionWrapper.wrap(
                session ->
                        session.createQuery(SELECT_BY_ID_QUERY)
                                .setParameter("Id", id)
                                .getSingleResult(), factory);
    }
}
