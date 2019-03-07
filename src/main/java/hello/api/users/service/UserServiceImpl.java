package hello.api.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hello.api.users.entity.User;
import hello.api.users.model.UserInfo;
import hello.api.users.repository.UserRepos;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl
        implements UserService {

    @Autowired
    private UserRepos userRepos;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Nullable
    @Override
    @Transactional(readOnly = true)
    public UserInfo findUserByUid(@Nonnull UUID uuid) {
        return createUserInfo(userRepos.findUsersByUid(uuid));
    }

    @Nullable
    @Override
    public UUID registrationUser(@Nonnull UserInfo userInfo) {



            if (userInfo.getUid() == null) userInfo.setUid(UUID.randomUUID());
            String encryptedPassword = passwordEncoder.encode(userInfo.getPassword());
            userInfo.setPassword(encryptedPassword);
            userRepos.saveAndFlush(createUser(userInfo));
            return userInfo.getUid();

    }

    @Nullable
    @Override
    public UserInfo loginUser(@Nonnull UserInfo userInfo) {
        User user = userRepos.findUsersByEmail(userInfo.getEmail());
     //   if (user.getPassword().equals(userInfo.getPassword())) {
        if(user!=null)
        {
            return createUserInfo(user);
        } else {
            return null;

        }
    }

    @Nullable
    @Override
    public void updateUser(@Nonnull UserInfo userInfo) {
        System.out.println("pass"+userInfo.getPassword());
        userRepos.updateUser(userInfo.getEmail(), userInfo.getVk(), userInfo.getPassword(), userInfo.getUid());
    }

    @Nullable
    @Override
    public UUID updateUuidUser(@Nonnull UUID uuid) {
       UUID newUiid = UUID.randomUUID();
        userRepos.updateUuidUser(uuid,newUiid);
        return newUiid;
    }
    @Nullable
    @Override
    public void updateVkUser(@Nonnull UserInfo userinfo) {
        userRepos.updateVkUser(userinfo.getVk(),userinfo.getUid());

    }

    @Nullable
    @Override
    public void deleteUser(@Nonnull UUID uuid) {
        userRepos.deleteByUid(uuid);
    }

    @Nonnull
    @Override
    public List<UserInfo> findAllUsers() {
        return userRepos.findAll()
                .stream()
                .map(this::createUserInfo)
                .collect(Collectors.toList());
    }

    @Nonnull
    private boolean createUser(@Nonnull String email, @Nonnull String password,
                               @Nonnull String vk, @Nonnull boolean identify) {

        User user = new User();

        user.setEmail(email);
        user.setPassword(password);
        user.setIdentify(identify);
        user.setUid(UUID.randomUUID());
        user.setVk(vk);
        return true;

    }

    @Nonnull
    private UserInfo createUserInfo(@Nonnull User user) {

        UserInfo userInfo = new UserInfo();
        userInfo.setVk(user.getVk());
        userInfo.setEmail(user.getEmail());
        userInfo.setPassword(user.getPassword());
        userInfo.setUid(user.getUid());
        return userInfo;
    }

    @Nonnull
    private User createUser(@Nonnull UserInfo userInfo) {

        User user = new User();
        user.setVk(userInfo.getVk());
        user.setEmail(userInfo.getEmail());
        user.setPassword(userInfo.getPassword());
        user.setUid(userInfo.getUid());
        return user;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
