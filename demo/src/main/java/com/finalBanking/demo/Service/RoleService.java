package com.finalBanking.demo.Service;

import com.finalBanking.demo.Dto.RoleRequest;
import com.finalBanking.demo.Dto.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequestDTO);
    List<RoleResponse> getAllRoles();
    RoleResponse assignPermissionsToRole(Long roleId, List<Long> permissionIds);
}
