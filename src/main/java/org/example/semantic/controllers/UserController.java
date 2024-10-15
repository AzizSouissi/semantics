package org.example.semantic.controller;

import org.example.semantic.models.User;
import org.example.semantic.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserModel userModel;

    public UserController(UserModel userModel) {
        this.userModel = userModel;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required.");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required.");
        }

        userModel.addUser(user.getUsername(), user.getPassword(), user.getRole());
        return ResponseEntity.ok("User added successfully!");
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return userModel.findUser(username)
                .map(userResource -> {
                    User userResponse = new User();
                    userResponse.setUsername(userResource.getURI().replace(userModel.getNs(), ""));
                    userResponse.setPassword(userResource.getProperty(userModel.getPasswordProp()).getString());
                    userResponse.setRole(userResource.getProperty(userModel.getRoleProp()).getString());
                    return ResponseEntity.ok(userResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        if (userModel.updateUser(user.getUsername(), user.getPassword(), user.getRole())) {
            return ResponseEntity.ok("User updated successfully!");
        }
        return ResponseEntity.notFound().body("User not found.");
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (userModel.deleteUser(username)) {
            return ResponseEntity.ok("User deleted successfully!");
        }
        return ResponseEntity.notFound().body("User not found.");
    }

    @GetMapping("/list")
    public ResponseEntity<String> listUsers() {
        return ResponseEntity.ok(userModel.listUsers());
    }
}
