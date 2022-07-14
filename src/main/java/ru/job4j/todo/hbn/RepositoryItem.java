package ru.job4j.todo.hbn;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.function.Function;

@Repository
public class RepositoryItem {

    private final SessionFactory sf;

    public RepositoryItem(SessionFactory sf) {
        this.sf = sf;
    }

    public void create(Item item) {
        this.factory(session -> session.save(item));
    }

    public List<Item> findAll() {
        return this.factory(session -> session.createQuery("from Item i order by i.id").list());
    }

    public Item findById(int id) {
        return (Item) this.factory(session -> session.createQuery("from Item i where i.id = :Id").
                setParameter("Id", id).getResultList().stream().findFirst().orElse(null));
    }

    public void replace(Item item) {
        this.factory(session -> session.createQuery("update Item i set i.name = :Name, i.description = :Description, i.done = :Done where i.id = :Id").
                setParameter("Id", item.getId()).
                setParameter("Name", item.getName()).
                setParameter("Description", item.getDescription()).
                setParameter("Done", item.isDone()).
                executeUpdate());
    }

    public void delete(int id) {
        this.factory(session -> session.createQuery("delete from Item i where i.id = :Id").
                setParameter("Id", id).
                executeUpdate());
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
