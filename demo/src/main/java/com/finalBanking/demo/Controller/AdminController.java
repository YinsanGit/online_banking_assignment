package com.finalBanking.demo.Controller;

import com.finalBanking.demo.Dto.RolePermissionRequest;
import com.finalBanking.demo.Entity.Permission;
import com.finalBanking.demo.Entity.Role;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Repository.permissionRepository;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Repository.userRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j

public class AdminController {
    private final roleRepository roleRepository;
    private final permissionRepository permissionRepository;
    private final userRepository userRepository;


    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@Valid @RequestBody RolePermissionRequest req) {
        try {
            Role role = roleRepository.findByName(req.name()).orElseGet(Role::new);
            role.setName(req.name());
            roleRepository.save(role);

            return ResponseEntity.ok(Map.of(
                    "message", "Role upserted",
                    "role", role.getName()
            ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "Role already exists or violates constraints"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to create or update role",
                    "details", e.getMessage()
            ));
        }
    }

    @PostMapping("/permissions")
    public ResponseEntity<?> createPermission(@Valid @RequestBody RolePermissionRequest req) {
        Permission p = permissionRepository.findByName(req.name()).orElseGet(Permission::new);
        p.setName(req.name());
        permissionRepository.save(p);
        return ResponseEntity.ok(Map.of("message", "Permission upserted", "permission", p.getName()));
    }

    @PostMapping("/roles/{roleName}/grant/{permName}")
    public ResponseEntity<?> grantPermissionToRole(@PathVariable String roleName, @PathVariable String permName) {
        Role role = roleRepository.findByName(roleName).orElseThrow();
        Permission perm = permissionRepository.findByName(permName).orElseThrow();
        role.getPermissions().add(perm);
        roleRepository.save(role);
        return ResponseEntity.ok(Map.of("message", "Permission granted to role"));
    }

    @PostMapping("/users/{userId}/assign/{roleName}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findByName(roleName).orElseThrow();
        user.getRoles().add(role);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Role assigned to user"));
    }




}
