package com.aurrix.slsample.todo.mvc.controllers;

import com.aurrix.slsample.todo.mvc.models.Todo;
import com.aurrix.slsample.todo.mvc.services.TodoStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc")
public class TodoController {


    @Autowired
    TodoStoreService todoStoreService;


    @GetMapping
    public String todoHome(
            Model model
    ) {
        model.addAttribute("todos", todoStoreService.getTodos());
        return "mvc";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("todo") Todo todo,
            Model model
    ) {
        todoStoreService.addTodo(todo);
        model.addAttribute("todos", todoStoreService.getTodos());
        return "redirect:/mvc";
    }
    @PostMapping("/change-status")
    public String updateTodo(
            @ModelAttribute("todo") Todo todo,
            Model model
    ) {
        todoStoreService.updateTodo(todo);
        model.addAttribute("todos", todoStoreService.getTodos());
        return "redirect:/mvc";
    }

    @PostMapping("/delete")
    public String deleteTodo(
            @ModelAttribute("id") String id,
            Model model
    ) {
        todoStoreService.deleteTodo(id);
        model.addAttribute("todos", todoStoreService.getTodos());
        return "redirect:/mvc";
    }
}
