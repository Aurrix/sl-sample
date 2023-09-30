package com.aurrix.slsample.todo.sl.models;

public record Todo(
        String id,
        String title,
        boolean done
) {
}
