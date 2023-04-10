package org.delfin.controller;

import org.delfin.constant.Endpoint;
import org.delfin.exception.UserNotFoundException;
import org.delfin.model.User;
import org.delfin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping(Endpoint.USER)
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(Endpoint.ID)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
         try {
             return ResponseEntity.ok(userService.findById(id));
         } catch (UserNotFoundException ex) {
             return ResponseEntity.badRequest().build();
         } catch (Exception ex) {
             return ResponseEntity.internalServerError().build();
         }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.save(user));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        try {
            return ResponseEntity.ok(userService.update(updatedUser));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(Endpoint.ID)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("authenticate/")
    public ResponseEntity<Void> authenticate(@RequestBody User request) {
        try {
            User user = userService.getUserByUserName(request.getUsername());
            // Verify the password using BCrypt
            if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                user.setFailedLogins(user.getFailedLogins() + 1);
                userService.update(user);
                return ResponseEntity.badRequest().build();
            }
            // Rest counter for failed attempts
            user.setFailedLogins(0);
            userService.update(user);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("request")
    public ResponseEntity<String> changePassword(@RequestParam("username") String userName, @RequestParam("email") String email) {
        try {
            userService.requestReset(userName, email);
            return ResponseEntity.ok("Requested PW reset!");
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("reset/{code}")
    public ResponseEntity<String> resetPassword(@PathVariable("code") String code, @RequestBody User user) {
        try {
            userService.resetPassword(code, user);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

