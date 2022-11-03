package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.util.SessionWrapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;

@Repository
public class ItemRepository {
    private static final String SELECT_ALL_QUERY = "from Item  item join fetch item.category join fetch item.priority";
    private static final String SELECT_BY_ID_QUERY = "from Item  item join fetch item.category join fetch item.priority"
            + " where item.id = :Id";
    private final SessionFactory factory;

    public ItemRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public void create(Item item, String timeZone) {
        try {
            SessionWrapper.zoneWrap(
                    session -> session.save(item),
                    factory, timeZone
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Item> findAll() {
        return SessionWrapper.wrap(session ->
                session.createQuery(SELECT_ALL_QUERY, Item.class)
                        .list(), factory);
    }

    public Item findById(int id) {
        return (Item) SessionWrapper.wrap(
                session ->
                        session.createQuery(SELECT_BY_ID_QUERY)
                                .setParameter("Id", id)
                                .getSingleResult(), factory);
    }

    public void update(Item item) {
        var findItem = findById(item.getId());
        if (findItem == null) {
            throw new NoSuchElementException("Invalid item id ");
        }
        SessionWrapper.wrap(session -> {
            item.setUser(findItem.getUser());
            item.setCreated(findItem.getCreated());
            session.saveOrUpdate(item);
            return null;
        }, factory);
    }

    public void delete(int id) {
        var findItem = findById(id);
        if (findItem == null) {
            throw new NoSuchElementException("Invalid item id ");
        }
        SessionWrapper.wrap(session -> {
            session.delete(findItem);
            return null;
        }, factory);
    }
}
