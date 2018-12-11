package hello.api.users.web;


import hello.api.users.model.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    private  final  Gson gson=new Gson();

    static final String URL_API_USERS = "http://localhost:8081/api-users/";

    static final String URL_API_USERS_LOGIN = URL_API_USERS.concat("login");
    static final String URL_API_USERS_REGISTR = URL_API_USERS.concat("create");
    static final String URL_API_USERS_UPDATE = URL_API_USERS.concat("update");
    static final String URL_API_USERS_UPDATE_UUID = URL_API_USERS.concat("updateUUID");
    static final String URL_API_USERS_GET = URL_API_USERS.concat("get");
    static final String URL_API_USERS_DELETE = URL_API_USERS.concat("delete");
    static final String URL_API_USERS_ALL = URL_API_USERS.concat("getAll");

    final String email= randomAlphabetic(3);
    final String vk="basta";
    final String password= randomAlphabetic(8);
    final UUID uuid=UUID.randomUUID();

    @Before
    public void setUp() throws Exception {

        UserInfo request= new UserInfo();
        request.setEmail(email);
        request.setVk(vk);
        request.setPassword(password);
        request.setUid(uuid);

        mvc.perform(post(URL_API_USERS_REGISTR).contentType(MediaType.APPLICATION_JSON).
                content(gson.toJson(request))
                        ).andDo(print())
                .andExpect(status().isCreated());
    }

    @After
    public void tearDown() throws Exception {
        mvc.perform(delete(URL_API_USERS_DELETE).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    public void getUser() throws Exception {


        mvc.perform(get(URL_API_USERS_GET).param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void updateUser() throws Exception {

        UserInfo request= new UserInfo();
        request.setEmail("12345@bk.ru");
       request.setVk(vk);
       request.setPassword(password);
        request.setUid(uuid);

        mvc.perform(patch(URL_API_USERS_UPDATE).contentType(MediaType.APPLICATION_JSON).
                content(gson.toJson(request))).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateUuidUser() throws Exception {

        UserInfo request= new UserInfo();
        request.setEmail(email);
        request.setVk(vk);
        request.setPassword(password);
        request.setUid(uuid);

        mvc.perform(patch(URL_API_USERS_UPDATE_UUID).contentType(MediaType.APPLICATION_JSON).
                content(gson.toJson(request))).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void loginUser() throws Exception {

        UserInfo request= new UserInfo();
        request.setEmail("1234@bk.ru");
        request.setPassword(password);

        mvc.perform(post(URL_API_USERS_LOGIN).contentType(MediaType.APPLICATION_JSON).
                content(gson.toJson(request))).andDo(print())
                .andExpect(status().isLocked());
    }

    @Test
    public void findAllUsers() throws Exception {

        mvc.perform(get(URL_API_USERS_ALL)).andDo(print())
                .andExpect(status().isOk());


    }
}