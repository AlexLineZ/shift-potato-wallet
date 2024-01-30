package ru.cft.template.model;

public record RegisterBody(
        Long phone,
        String password,
        String firstName,
        String lastName,
        String email,
        Integer age
) { }
