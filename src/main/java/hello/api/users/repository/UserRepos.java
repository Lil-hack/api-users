package hello.api.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hello.api.users.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.UUDecoder;

import java.util.UUID;

public interface UserRepos
        extends JpaRepository<User, Integer> {


    User findUsersByUid(UUID uuid);

    User findUsersByEmail(String email);

    @Transactional
    void deleteByUid(UUID uuid);




    @Transactional
    @Modifying
    @Query("update User t   set t.email = case  when :email  is not null then :email  end,\n" +
            "    t.password= case when :password is not null then :password end,\n" +
            "            t.vk=case when :vk is not null then :vk end\n" +
            "     where t.uid = :uid")
    void updateUser(@Param("email") String email, @Param("vk") String vk,
                    @Param("password") String password, @Param("uid") UUID uid);

    @Transactional
    @Modifying
    @Query("update User t   set t.uid=:newUid" +
            "     where t.uid = :uid")
    void updateUuidUser(@Param("uid") UUID uuid, @Param("newUid") UUID newUuid);

    @Transactional
    @Modifying
    @Query("update User t   set t.vk=:vk" +
            "     where t.uid = :uid")
    void updateVkUser(@Param("vk") String vk, @Param("uid") UUID uid);

}