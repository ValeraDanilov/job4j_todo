package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.filter.FilterOptions;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ItemService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class IndexController {

    private final ItemService itemService;

    public IndexController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("items", this.itemService.findAllByFilter(FilterOptions.ALL)
                .stream()
                .filter(post -> date(post.getCreated()))
                .toList());
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
        return "index";
    }

    private boolean date(LocalDateTime date) {
        return date.getYear() == LocalDateTime.now().getYear()
                && date.getMonth() == LocalDateTime.now().getMonth()
                && date.getDayOfMonth() == LocalDateTime.now().getDayOfMonth();
    }
}
