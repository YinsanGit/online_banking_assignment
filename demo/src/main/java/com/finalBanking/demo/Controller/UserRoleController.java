package com.finalBanking.demo.Controller;

import com.finalBanking.demo.Entity.Permission;
import com.finalBanking.demo.Entity.Role;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")

public class UserRoleController {
    @Autowired
    private userRepository userRepository;

    @Autowired
    private roleRepository roleRepository;

    // Assign roles to a user
    @PostMapping("/{userId}/roles")
    public ResponseEntity<Map<String, Object>> assignRolesToUser(
            @PathVariable Long userId,
            @RequestBody List<String> roleNames) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        response.put("message", "Roles assigned successfully");

        return ResponseEntity.ok(response);
    }

    // Get all users with their roles
    @GetMapping("/with-roles")
    public ResponseEntity<List<Map<String, Object>>> getAllUsersWithRoles() {
        List<User> users = userRepository.findAll();

        List<Map<String, Object>> response = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());
            userMap.put("roles", user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));
            return userMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Get a specific user with roles
    @GetMapping("/{userId}/roles")
    public ResponseEntity<Map<String, Object>> getUserRoles(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

        List<Map<String, Object>> roles = user.getRoles().stream()
                .map(role -> {
                    Map<String, Object> roleMap = new HashMap<>();
                    roleMap.put("id", role.getId());
                    roleMap.put("name", role.getName());
                    roleMap.put("description", role.getDescription());
                    roleMap.put("permissions", role.getPermissions().stream()
                            .map(Permission::getName)
                            .collect(Collectors.toList()));
                    return roleMap;
                })
                .collect(Collectors.toList());

        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }

    // Remove a role from a user
    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<Map<String, Object>> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable String roleName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role roleToRemove = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().remove(roleToRemove);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Role removed successfully");
        response.put("userId", user.getId());
        response.put("removedRole", roleName);
        response.put("remainingRoles", user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}


