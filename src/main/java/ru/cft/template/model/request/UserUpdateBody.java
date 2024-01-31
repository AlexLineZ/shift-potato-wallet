package ru.cft.template.model.request;


public record UserUpdateBody(
        String firstName,
        String lastName,
        String email
) {
}
