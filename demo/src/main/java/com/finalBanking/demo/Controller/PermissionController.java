package com.finalBanking.demo.Controller;


import com.finalBanking.demo.Dto.PermissionRequest;
import com.finalBanking.demo.Dto.PermissionResponse;
import com.finalBanking.demo.Exception.ApiResponseEntityDto;
import com.finalBanking.demo.Exception.ApiResponseUtil;
import com.finalBanking.demo.Service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseEntityDto> createPermission(@RequestBody PermissionRequest permissionRequestDTO) {

        try{
            PermissionResponse createdPermission = permissionService.createPermission(permissionRequestDTO);
            return ResponseEntity.ok(ApiResponseUtil.successResponse(createdPermission));
        }
        catch (Exception e){
            ApiResponseEntityDto errorBody = ApiResponseUtil.createApiResponseEntityDto(
                        "500",
                        500,
                        "Internal Server Error",
                        e.getMessage(),
                        null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }

    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
}
