package com.hasbi.taskmanager.controller;

import org.springframework.http.ResponseEntity;

public final class ControllerResponseBuilder {

    private ControllerResponseBuilder() {
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}
