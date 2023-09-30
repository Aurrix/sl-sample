package com.aurrix.slsample.todo.mvc.models;

public record Todo(
        String id,
        String title,
        boolean done
) {
}
