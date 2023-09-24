package com.aurrix.slsample.todo.models;

public record Todo(
        String id,
        String title,
        String description,
        boolean done
) {
}
