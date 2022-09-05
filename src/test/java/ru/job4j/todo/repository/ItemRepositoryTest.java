package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.todo.model.Item;

import java.time.LocalDateTime;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ItemRepositoryTest {
    private static SessionFactory sessionFactory;

    private static ItemRepository todo;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        todo = new ItemRepository(sessionFactory);
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from Item ").executeUpdate();
        transaction.commit();
    }

    @Test
    public void create() {
        Item item = new Item(0, "test1", "test", LocalDateTime.now(), false);
        todo.create(item);
        Item result = todo.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test
    public void findAll() {
        Item item = new Item(0, "test", "test", LocalDateTime.now(), false);
        Item itemFirst = new Item(0, "test1", "test1", LocalDateTime.now(), false);
        Item itemSecond = new Item(0, "test2", "test2", LocalDateTime.now(), false);
        todo.create(item);
        todo.create(itemFirst);
        todo.create(itemSecond);
        List<Item> itemList = List.of(
                new Item(item.getId(), "test", "test", LocalDateTime.now(), false),
                new Item(itemFirst.getId(), "test1", "test1", LocalDateTime.now(), false),
                new Item(itemSecond.getId(), "test2", "test2", LocalDateTime.now(), false));
        assertThat(itemList, is(todo.findAll()));
    }

    @Test
    public void findById() {
        Item item = new Item(0, "test1", "test", LocalDateTime.now(), false);
        todo.create(item);
        Item result = todo.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test
    public void update() {
        Item item = new Item(0, "test", "test", LocalDateTime.now(), false);
        todo.create(item);
        item.setName("test1");
        todo.update(item);
        Item result = todo.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test(expected = NullPointerException.class)
    public void delete() {
        Item item = new Item(0, "test", "test", LocalDateTime.now(), false);
        todo.create(item);
        todo.delete(item.getId());
        Item result = todo.findById(item.getId());
        assertNull(result.getName());
    }
}
