package hello.api.users.web;

import hello.api.users.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import hello.api.users.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api-users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    private UserService userService;



    @GetMapping("/get{uuid}")
    public ResponseEntity<UserInfo> getUser(@RequestParam UUID uuid) {
        try {


            return new ResponseEntity(userService.findUserByUid(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getError", e);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createUser(@RequestBody UserInfo requestUserDetails) {
        try {


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
    public ResponseEntity deleteUser(@RequestParam UUID uuid) {

        try {


            userService.deleteUser(uuid);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("deleteError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity updateUser(@RequestBody UserInfo requestUserDetails) {
        try {


            userService.updateUser(requestUserDetails);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("updateUserError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUUID")
    public ResponseEntity updateUuidUser(@RequestBody UserInfo requestUserDetails) {
        try {


           UUID newUuid=userService.updateUuidUser(requestUserDetails.getUid());

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("updateUuidUserError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateVK")
    public ResponseEntity updateVKUser(@RequestBody UserInfo requestUserDetails) {
        try {


            userService.updateVkUser(requestUserDetails);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("updatevkUserError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<UserInfo> loginUser(@RequestBody UserInfo requestUserDetails) {
        try {
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
    public ResponseEntity<List<UserInfo>> findAllUsers() {
        try {


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
