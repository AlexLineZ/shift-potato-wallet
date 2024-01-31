package ru.cft.template.model;


public record UserUpdateBody(
        String firstName,
        String lastName,
        String email
) {
}
