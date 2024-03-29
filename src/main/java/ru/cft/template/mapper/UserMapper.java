package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.User;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.response.UserResponse;

@Component
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

    public static UserResponse mapUserToResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getWallet().getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRegistrationDate(),
                user.getLastUpdateDate(),
                user.getAge()
        );
    }
}
