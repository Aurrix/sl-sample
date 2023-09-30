package com.aurrix.slsample.todo.mvc.services;

import com.aurrix.slsample.todo.mvc.models.Todo;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Scope("session")
public class TodoStoreService {

    public List<Todo> getTodos() {
        var todos = TodoStoreService.session().getAttribute("todos");
        if (todos == null) {
            todos = new ArrayList<>() {{
                add(new Todo("1", "DEFAULT", false));
            }};
            TodoStoreService.session().setAttribute("todos", todos);
        }
        return (List<Todo>) todos;
    }

    public void deleteTodo(String id) {
        var todos = (List<Todo>) TodoStoreService.session().getAttribute("todos");
        if (todos == null) return;
        var todo = getById(id, todos).orElseThrow();
        todos.remove(todo);
        TodoStoreService.session().setAttribute("todos", todos);
    }

    public void updateTodo(Todo todo) {
        var todos = (List<Todo>) TodoStoreService.session().getAttribute("todos");
        if (todos == null) return;
        var oldTodo = getById(todo.id(), todos).orElseThrow();
        todos.remove(oldTodo);
        todos.add(todo);
        TodoStoreService.session().setAttribute("todos", todos);
    }

    public void addTodo(Todo todo) {
        var todos = (List<Todo>) TodoStoreService.session().getAttribute("todos");
        if (todos == null) return;
        todos.add(new Todo(
                UUID.randomUUID().toString(),
                todo.title(),
                todo.done()
        ));
        TodoStoreService.session().setAttribute("todos", todos);
    }

    private Optional<Todo> getById(String id, List<Todo> todos) {
        return todos.stream().filter(t -> t.id().equals(id)).findFirst();
    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

}
