package hello.api.users.web;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.api.users.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import hello.api.users.service.UserService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api-users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    private UserService userService;
    static final String ACCESS_TOKEN_VALIDATE_URI="https://api-my-gateway.herokuapp.com/api-gateway/oauth20/tokens/validate";
    static final String CLIENT_ID="9aa3ae11759ae257e2f29484a32820845ae59763";
    static final String CLIENT_SECRET="3997673d7814bbbcde139fe181e7fba723beb70a4e6e49363230ff78051f40d1";

    private boolean OauthCheckToken(String token)
    {

        if(token==null)
            return false;
        else {
            try {
                String token2 = token.replace("Bearer ", "");
                System.out.println(token2);
                logger.info("oauth"+ token2);
                logger.debug("oauth"+ token2);
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ACCESS_TOKEN_VALIDATE_URI)
                        .queryParam("token", token2);
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(builder.toUriString(), String.class);
                logger.info("result"+ result);
                logger.debug("result"+ result);
                JsonFactory factory = new JsonFactory();

                ObjectMapper mapper = new ObjectMapper(factory);
                JsonNode rootNode = mapper.readTree(result);

                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();

                while (fieldsIterator.hasNext()) {

                    Map.Entry<String, JsonNode> field = fieldsIterator.next();

                    System.out.println(field.getKey());
                    if (field.getKey() == "error") {
                        return false;
                    }

                }
                return  true;
            } catch (Exception e) {
                logger.error("oauth", e);
                return  false;
            }
        }
    }

    @GetMapping("/get{uuid}")
    public ResponseEntity<UserInfo> getUser(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        try {
            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);

            return new ResponseEntity(userService.findUserByUid(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getError", e);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        try {
            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);

            UUID uuid= userService.registrationUser(requestUserDetails);
            if (uuid != null) {
                return new ResponseEntity(uuid,HttpStatus.CREATED);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete{uuid}")
    public ResponseEntity deleteUser(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {

        try {
            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);

            userService.deleteUser(uuid);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("deleteError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity updateUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        try {

            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);
            userService.updateUser(requestUserDetails);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("updateUserError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUUID")
    public ResponseEntity updateUuidUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        try {
            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);

           UUID newUuid=userService.updateUuidUser(requestUserDetails.getUid());

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("updateUuidUserError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateVK")
    public ResponseEntity updateVKUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        try {

            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);
            userService.updateVkUser(requestUserDetails);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("updatevkUserError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<UserInfo> loginUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        try {
            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);
           UserInfo us= userService.loginUser(requestUserDetails);
            if (us==null) {
                return new ResponseEntity(HttpStatus.LOCKED);

            } else {
                return new ResponseEntity(us,HttpStatus.OK);
            }

        } catch (Exception e) {
            logger.error("loginError", e);
            return new ResponseEntity(HttpStatus.LOCKED);
           //
            // return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<UserInfo>> findAllUsers(@RequestHeader(value="Authorization",required = false) String token) {
        try {
            if(!OauthCheckToken(token))
                return   new ResponseEntity(HttpStatus.UNAUTHORIZED);

            List<UserInfo> users = userService.findAllUsers();

            if (users.isEmpty()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);

            } else {
                return new ResponseEntity<List<UserInfo>>(users, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("getAllError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
