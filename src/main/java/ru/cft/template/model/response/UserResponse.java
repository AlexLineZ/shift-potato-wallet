package ru.cft.template.model.response;

import java.util.Date;

public record UserResponse(
        String id,
        String walletId,
        String firstName,
        String lastName,
        String fullName,
        String email,
        Long phone,
        Date registrationDate,
        Date lastUpdateDate,
        Integer age
) { }
