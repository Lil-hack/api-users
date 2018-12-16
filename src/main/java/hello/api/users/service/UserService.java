package hello.api.users.service;

import hello.api.users.model.UserInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface UserService {


    @Nonnull
    List<UserInfo> findAllUsers();

    UserInfo findUserByUid(@Nonnull UUID uuid);

    @Nullable
    UUID registrationUser(@Nonnull UserInfo userinfo);


    UserInfo loginUser(@Nonnull UserInfo userinfo);


    void updateUser(@Nonnull UserInfo userinfo);

    UUID updateUuidUser(@Nonnull UUID uuid);

    void deleteUser(@Nonnull UUID uuid);

    void updateVkUser(@Nonnull UserInfo userinfo);
}
