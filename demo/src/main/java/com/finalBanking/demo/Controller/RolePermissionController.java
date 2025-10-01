package com.finalBanking.demo.Controller;

import com.finalBanking.demo.Dto.RoleRequest;
import com.finalBanking.demo.Dto.RoleResponse;
import com.finalBanking.demo.Entity.Permission;
import com.finalBanking.demo.Entity.Role;
import com.finalBanking.demo.Exception.ApiResponseEntityDto;
import com.finalBanking.demo.Exception.ApiResponseUtil;
import com.finalBanking.demo.Exception.CustomErrorException;
import com.finalBanking.demo.Repository.permissionRepository;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RolePermissionController {

    @Autowired
    private roleRepository roleRepository;

    @Autowired
    private permissionRepository permissionRepository;

    // Get all roles with their permissions
    @GetMapping("/roles")
    public ResponseEntity<List<Map<String, Object>>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        List<Map<String, Object>> response = roles.stream().map(role -> {
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("id", role.getId());
            roleMap.put("name", role.getName());
            roleMap.put("description", role.getDescription());
            roleMap.put("createdAt", role.getCreatedAt());
            roleMap.put("permissions", role.getPermissions().stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList()));
            return roleMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Get a specific role by name
    @GetMapping("/roles/{roleName}")
    public ResponseEntity<Map<String, Object>> getRoleByName(@PathVariable String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("id", role.getId());
        roleMap.put("name", role.getName());
        roleMap.put("description", role.getDescription());
        roleMap.put("createdAt", role.getCreatedAt());

        List<Map<String, String>> permissions = role.getPermissions().stream()
                .map(p -> {
                    Map<String, String> permMap = new HashMap<>();
                    permMap.put("name", p.getName());
                    permMap.put("description", p.getDescription());
                    permMap.put("category", p.getCategory());
                    return permMap;
                })
                .collect(Collectors.toList());

        roleMap.put("permissions", permissions);

        return ResponseEntity.ok(roleMap);
    }

    // Get all permissions
    @GetMapping("/permissions")
    public ResponseEntity<List<Map<String, Object>>> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();

        List<Map<String, Object>> response = permissions.stream().map(permission -> {
            Map<String, Object> permMap = new HashMap<>();
            permMap.put("id", permission.getId());
            permMap.put("name", permission.getName());
            permMap.put("description", permission.getDescription());
            permMap.put("category", permission.getCategory());
            return permMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Get permissions by category
    @GetMapping("/permissions/category/{category}")
    public ResponseEntity<List<Map<String, Object>>> getPermissionsByCategory(@PathVariable String category) {
        List<Permission> permissions = permissionRepository.findAll().stream()
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList());

        List<Map<String, Object>> response = permissions.stream().map(permission -> {
            Map<String, Object> permMap = new HashMap<>();
            permMap.put("id", permission.getId());
            permMap.put("name", permission.getName());
            permMap.put("description", permission.getDescription());
            permMap.put("category", permission.getCategory());
            return permMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}


