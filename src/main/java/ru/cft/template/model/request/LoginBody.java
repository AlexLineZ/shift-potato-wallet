package ru.cft.template.model.request;

public record LoginBody(
     Long phone,
     String password
) {
}
