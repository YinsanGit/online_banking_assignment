package com.finalBanking.demo.Controller;


import com.finalBanking.demo.Dto.RoleRequest;
import com.finalBanking.demo.Dto.RoleResponse;
import com.finalBanking.demo.Exception.ApiResponseEntityDto;
import com.finalBanking.demo.Exception.ApiResponseUtil;
import com.finalBanking.demo.Exception.CustomErrorException;
import com.finalBanking.demo.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponseEntityDto> createRole(@RequestBody RoleRequest roleRequestDTO) {
        RoleResponse createdRole = roleService.createRole(roleRequestDTO);
        return ResponseEntity.ok(ApiResponseUtil.successResponse(createdRole));

    }

//    @GetMapping("/getAll")
//    public ResponseEntity<List<RoleResponse>> getAllRoles() {
//        List<RoleResponse> roles = roleService.getAllRoles();
//        return ResponseEntity.ok(roles);
//    }

    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponse> assignPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {

        RoleResponse updatedRole = roleService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok(updatedRole);
    }
}
