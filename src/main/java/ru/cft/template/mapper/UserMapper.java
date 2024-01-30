package ru.cft.template.mapper;

import ru.cft.template.entity.User;
import ru.cft.template.model.RegisterBody;

public class UserMapper {
    public static User mapRegisterBodyToUser(RegisterBody body) {
        User user = new User();
        user.setAge(body.age());
        user.setPassword(body.password());
        user.setFirstName(body.firstName());
        user.setLastName(body.lastName());
        user.setEmail(body.email());
        user.setPhone(body.phone());
        return user;
    }
}
