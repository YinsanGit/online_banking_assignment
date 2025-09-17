package com.finalBanking.demo.Controller;


import com.finalBanking.demo.Dto.PermissionRequest;
import com.finalBanking.demo.Dto.PermissionResponse;
import com.finalBanking.demo.Service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequestDTO) {
        PermissionResponse createdPermission = permissionService.createPermission(permissionRequestDTO);
        return ResponseEntity.ok(createdPermission);
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
}
