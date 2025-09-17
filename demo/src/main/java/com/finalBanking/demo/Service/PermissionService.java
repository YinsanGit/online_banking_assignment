package com.finalBanking.demo.Service;

import com.finalBanking.demo.Dto.PermissionRequest;
import com.finalBanking.demo.Dto.PermissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequestDTO);
    List<PermissionResponse> getAllPermissions();
}
