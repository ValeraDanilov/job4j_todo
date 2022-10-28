package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.filter.FilterOptions;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.ItemService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ItemController {

    private final ItemService service;
    private final UserService userService;
    private final CategoryService categoryService;
    private final PriorityService priorityService;
    private static final List<String> FILTER = Arrays.stream(FilterOptions.values())
            .map(FilterOptions::getValue)
            .toList();
    private final AtomicInteger userId = new AtomicInteger();
    private static final String PATH = "redirect:/items";
    private static final String ITEMS = "items";
    private static final String CATEGORY = "category";
    private static final String PRIORITY = "priority";
    private static final String CHOOSES = "chooses";

    public ItemController(ItemService service, UserService userService, CategoryService categoryService, PriorityService priorityService) {
        this.service = service;
        this.userService = userService;
        this.categoryService = categoryService;
        this.priorityService = priorityService;
    }

    @GetMapping("/items")
    public String items(Model model, HttpSession session) {
        model.addAttribute(ITEMS, this.service.findAllByFilter(FilterOptions.ALL));
        model.addAttribute("chooses", FILTER);
        model.addAttribute("category", this.categoryService.findAll());
        model.addAttribute(ITEMS, this.service.findAllByFilter(FilterOptions.ALL));
        model.addAttribute(CHOOSES, FILTER);
        model.addAttribute(CATEGORY, this.categoryService.findAll());
        model.addAttribute(PRIORITY, this.priorityService.findAll());
        sessions(model, session);
        return ITEMS;
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item,
                             @RequestParam(value = "category.id", required = false) List<Integer> categoriesId,
                             HttpSession session, Model model) {
        Set<Category> categories = new HashSet<>();
        if (categoriesId != null) {
            categoriesId.forEach(value -> categories.add(this.categoryService.findById(value)));
            item.setCategory(categories);
        }
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
        model.addAttribute(ITEMS, this.service.findAllByFilter(FilterOptions.valueOf(id.toUpperCase(Locale.ROOT))));
        model.addAttribute("selectedId", id);
        model.addAttribute(CHOOSES, FILTER);
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
        model.addAttribute(CATEGORY, this.categoryService.findAll());
        model.addAttribute(PRIORITY, this.priorityService.findAll());
        sessions(model, session);
        return "updateItem";
    }

    @PostMapping("/updateItem")
    public String updateItem(@ModelAttribute Item item,
                             @RequestParam(value = "category.id", required = false) List<Integer> categoriesId) {
        Set<Category> categories = new HashSet<>();
        if (categoriesId != null) {
            categoriesId.forEach(value -> categories.add(this.categoryService.findById(value)));
            item.setCategory(categories);
        }
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
