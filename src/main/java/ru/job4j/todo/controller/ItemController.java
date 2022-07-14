package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ServiceItem;

import java.time.LocalDateTime;

@Controller
public class ItemController {

    private final ServiceItem service;
    public final String[] value = new String[]{"Все", "Новые", "Выполненные"};
    private static final String PATH = "redirect:/items";
    private static final String ITEMS = "items";

    public ItemController(ServiceItem service) {
        this.service = service;
    }

    @GetMapping("/items")
    public String items(Model model) {
        model.addAttribute(ITEMS, this.service.findAll("Все"));
        model.addAttribute("chooses", this.value);
        return ITEMS;
    }

    @GetMapping("/formAddItem")
    public String addItem(Model model) {
        model.addAttribute("item", new Item(0, "Заполните поле", "Заполните поле", null, false));
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
    public String sortItemCondition(Model model, @PathVariable Integer id) {
        model.addAttribute(ITEMS, this.service.findAll(this.value[id]));
        String res = this.value[0];
        this.value[0] = this.value[id];
        this.value[id] = res;
        model.addAttribute("chooses", this.value);
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
        this.service.replace(item);
        return String.format("redirect:/formItemId/%s", item.getId());
    }

    @GetMapping("/formExecuteItem/{itemId}")
    public String executeItem(@PathVariable("itemId") int id) {
        Item res = this.service.findById(id);
        res.setDone(true);
        this.service.replace(res);
        return PATH;
    }
}
