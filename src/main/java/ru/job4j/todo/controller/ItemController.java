package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.filter.FilterOptions;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ItemService;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ItemController {

    private final ItemService service;
    private final UserService userService;
    private static final List<String> FILTER = Arrays.stream(FilterOptions.values())
            .map(FilterOptions::getValue)
            .toList();
    private final AtomicInteger userId = new AtomicInteger();
    private static final String PATH = "redirect:/items";
    private static final String ITEMS = "items";

    public ItemController(ItemService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/items")
    public String items(Model model, HttpSession session) {
        model.addAttribute(ITEMS, this.service.findAll(FilterOptions.ALL));
        model.addAttribute("chooses", FILTER);
        sessions(model, session);
        return ITEMS;
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item, Model model, HttpSession session) {
        item.setCreated(LocalDateTime.now());
        item.setUser(this.userService.findById(this.userId.get()));
        this.service.create(item);
        sessions(model, session);
        return "items";
    }

    @GetMapping("/formItemId/{id}")
    public String searchItem(Model model, @PathVariable Integer id, HttpSession session) {
        model.addAttribute("item", this.service.findById(id));
        sessions(model, session);
        return "item";
    }

    @GetMapping("/formItemCondition/{id}")
    public String sortItemCondition(Model model, @PathVariable String id, HttpSession session) {
        model.addAttribute(ITEMS, this.service.findAll(FilterOptions.valueOf(id.toUpperCase(Locale.ROOT))));
        model.addAttribute("selectedId", id);
        model.addAttribute("chooses", FILTER);
        sessions(model, session);
        return ITEMS;
    }

    @GetMapping("/formDeleteItem/{itemId}")
    public String formDeleteItem(@PathVariable("itemId") int id) {
        this.service.delete(id);
        return PATH;
    }

    @GetMapping("/formUpdateItem/{itemId}")
    public String formUpdateItem(Model model, @PathVariable("itemId") int id, HttpSession session) {
        model.addAttribute(ITEMS, this.service.findById(id));
        sessions(model, session);
        return "updateItem";
    }

    @PostMapping("/updateItem")
    public String updateItem(@ModelAttribute Item item) {
        this.service.update(item);
        return String.format("redirect:/formItemId/%s", item.getId());
    }

    @GetMapping("/formExecuteItem/{itemId}")
    public String executeItem(@PathVariable("itemId") int id) {
        Item res = this.service.findById(id);
        res.setDone(true);
        this.service.update(res);
        return PATH;
    }

    private void sessions(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
        this.userId.set(user.getId());
    }
}
