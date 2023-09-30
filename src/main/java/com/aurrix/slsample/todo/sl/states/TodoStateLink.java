package com.aurrix.slsample.todo.sl.states;

import com.aurrix.slsample.sml.annotations.StateDefinition;
import com.aurrix.slsample.sml.models.State;
import com.aurrix.slsample.todo.sl.models.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@StateDefinition
public class TodoStateLink extends State {
    List<Todo> todos = new ArrayList<>();

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public void onInit() {
        todos = List.of(
                new Todo(
                        "1",
                        "DEFAULT",
                        false
                )
        );
    }

    @Override
    public String getKey() {
        return "todos_state";
    }

    @Override
    public String toString() {
        return """
                 [
                 %s
                 ]
                """.formatted(this.todos.stream().map(Todo::toString).collect(Collectors.joining(",\n")));
    }
}
