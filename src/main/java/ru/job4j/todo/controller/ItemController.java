package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.FilterOptions;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class ItemController {

    private final ItemService service;
    private static final List<String> FILTER = Arrays.stream(FilterOptions.values())
            .map(FilterOptions::getValue)
            .toList();
    private static final String PATH = "redirect:/items";
    private static final String ITEMS = "items";

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/items")
    public String items(Model model) {
        model.addAttribute(ITEMS, this.service.findAll(FilterOptions.ALL));
        model.addAttribute("chooses", FILTER);
        return ITEMS;
    }

    @GetMapping("/formAddItem")
    public String addItem(Model model) {
        model.addAttribute("item", new Item(0, "Fill in the field", "Fill in the field", null, false));
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item) {
        item.setCreated(LocalDateTime.now());
        this.service.create(item);
        return PATH;
    }

    @GetMapping("/formItemId/{id}")
    public String searchItem(Model model, @PathVariable Integer id) {
        model.addAttribute("item", this.service.findById(id));
        return "item";
    }

    @GetMapping("/formItemCondition/{id}")
    public String sortItemCondition(Model model, @PathVariable String id) {
        model.addAttribute(ITEMS, this.service.findAll(FilterOptions.valueOf(id.toUpperCase(Locale.ROOT))));
        model.addAttribute("selectedId", id);
        model.addAttribute("chooses", FILTER);
        return ITEMS;
    }

    @GetMapping("/formDeleteItem/{itemId}")
    public String formDeleteItem(@PathVariable("itemId") int id) {
        this.service.delete(id);
        return PATH;
    }

    @GetMapping("/formUpdateItem/{itemId}")
    public String formUpdateItem(Model model, @PathVariable("itemId") int id) {
        model.addAttribute(ITEMS, this.service.findById(id));
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
}
