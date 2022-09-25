package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;


public class ItemRepositoryTest {
    private static SessionFactory sessionFactory;
    private static UserRepository user;

    private static ItemRepository todo;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        todo = new ItemRepository(sessionFactory);
        user = new UserRepository(sessionFactory);
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from Item ").executeUpdate();
        transaction.commit();
        var userTransaction = session.beginTransaction();
        session.createQuery("delete from User ").executeUpdate();
        transaction.commit();
    }
/*
    @Ignore
    public void create() {
        User newUser = new User(0, "name", "3@3", "1");
        user.create(newUser);
        Item item = new Item(0, newUser, "test1", "test", LocalDateTime.now(), false);
        todo.create(item);
        Item result = todo.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Ignore
    public void findAll() {
        User newUser = new User(0, "name", "3@3", "1");
        user.create(newUser);
        Item item = new Item(0, newUser, "test", "test", LocalDateTime.now(), false);
        Item itemFirst = new Item(0, newUser, "test1", "test1", LocalDateTime.now(), false);
        Item itemSecond = new Item(0, newUser, "test2", "test2", LocalDateTime.now(), false);
        todo.create(item);
        todo.create(itemFirst);
        todo.create(itemSecond);
        List<Item> itemList = List.of(
                new Item(item.getId(), newUser, "test", "test", LocalDateTime.now(), false),
                new Item(itemFirst.getId(), newUser, "test1", "test1", LocalDateTime.now(), false),
                new Item(itemSecond.getId(), newUser, "test2", "test2", LocalDateTime.now(), false));
        assertThat(itemList, is(todo.findAll()));
    }

    @Ignore
    public void findById() {
        User newUser = new User(0, "name", "3@3", "1");
        user.create(newUser);
        Item item = new Item(0, newUser, "test1", "test", LocalDateTime.now(), false);
        todo.create(item);
        Item result = todo.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Ignore
    public void update() {
        User newUser = new User(0, "name", "3@3", "1");
        user.create(newUser);
        Item item = new Item(0, newUser, "test", "test", LocalDateTime.now(), false);
        todo.create(item);
        item.setName("test1");
        todo.update(item);
        Item result = todo.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test(expected = NullPointerException.class)
    public void delete() {
        User newUser = new User(0, "name", "3@3", "1");
        user.create(newUser);
        Item item = new Item(0, newUser, "test", "test", LocalDateTime.now(), false);
        todo.create(item);
        todo.delete(item.getId());
        Item result = todo.findById(item.getId());
        assertNull(result.getName());
    }

 */
}
