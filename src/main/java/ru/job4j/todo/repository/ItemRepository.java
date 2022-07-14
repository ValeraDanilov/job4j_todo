package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.function.Function;

@Repository
public class ItemRepository {

    private static final String DELETE_QUERY = "delete from Item i where i.id = :Id";
    private static final String UPDATE_QUERY = "update Item i " +
            "set i.name = :Name, i.description = :Description, i.done = :Done" +
            " where i.id = :Id";
    private static final String SELECT_ALL_QUERY = "from Item i order by i.id";
    private static final String SELECT_BY_ID_QUERY = "from Item i where i.id = :Id";
    private final SessionFactory sf;

    public ItemRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public void create(Item item) {
        this.factory(session ->
                session.save(item));
    }

    public List<Item> findAll() {
        return this.factory(session ->
                session.createQuery(SELECT_ALL_QUERY)
                        .list());
    }

    public Item findById(int id) {
        return (Item) this.factory(
                session ->
                        session.createQuery(SELECT_BY_ID_QUERY)
                                .setParameter("Id", id)
                                .getResultList().stream()
                                .findFirst().orElse(null));
    }

    public void update(Item item) {
        this.factory(session -> session.createQuery(
                        UPDATE_QUERY).
                setParameter("Id", item.getId())
                .setParameter("Name", item.getName())
                .setParameter("Description", item.getDescription())
                .setParameter("Done", item.isDone())
                .executeUpdate());
    }

    public void delete(int id) {
        this.factory(session ->
                session.createQuery(DELETE_QUERY)
                        .setParameter("Id", id)
                        .executeUpdate());
    }

    private <T> T factory(Function<Session, T> command) {
        final Session session = this.sf.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (Exception eo) {
            session.getTransaction().rollback();
            throw eo;
        } finally {
            session.close();
        }
    }
}
