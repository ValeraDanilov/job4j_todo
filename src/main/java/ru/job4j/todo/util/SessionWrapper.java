package ru.job4j.todo.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.TimeZone;
import java.util.function.Function;

public class SessionWrapper {

    public static <T> T wrap(Function<Session, T> command, SessionFactory sf) {
        final Session session = sf.openSession();
        return wrap(command, session);
    }

    public static <T> void zoneWrap(Function<Session, T> command, SessionFactory sf, String timeZone) {
        final Session session = sf.withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone(timeZone))
                .openSession();
        wrap(command, session);
    }

    private static <T> T wrap(Function<Session, T> command, Session session) {
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
