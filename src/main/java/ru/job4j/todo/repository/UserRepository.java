package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class UserRepository {

    private static final String SELECT_ALL_QUERY = "from User i order by i.id";
    private static final String SELECT_BY_ID_QUERY = "from User i where i.id = :Id";
    private static final String SELECT_BY_EMAIL_AND_PASSWORD_QUERY = "from User i where i.email = :email1 and i.password = :password1";
    private final SessionFactory sf;

    public UserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public <T> Optional<T> create(User user) {
        try {
            T finalUser = (T) this.factory(session ->
                    session.save(user));
            return Optional.of(finalUser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        return this.factory(session ->
                session.createQuery(SELECT_ALL_QUERY)
                        .list());
    }

    public User findById(int id) {
        return (User) this.factory(
                session ->
                        session.createQuery(SELECT_BY_ID_QUERY)
                                .setParameter("Id", id)
                                .getResultList().stream()
                                .findFirst().orElse(null));
    }

    public User findUserByEmailAndPwd(String email, String password) {
        return (User) this.factory(
                session ->
                        session.createQuery(SELECT_BY_EMAIL_AND_PASSWORD_QUERY)
                                .setParameter("email1", email)
                                .setParameter("password1", password)
                                .getResultList()
                                .stream().
                                findFirst()
                                .orElse(null));
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
