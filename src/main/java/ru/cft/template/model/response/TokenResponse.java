package ru.cft.template.model.response;

import lombok.Builder;

@Builder
public record TokenResponse(String token) { }
