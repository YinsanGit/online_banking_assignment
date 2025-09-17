package com.finalBanking.demo.Service.Impl;

import com.finalBanking.demo.Dto.PermissionResponse;
import com.finalBanking.demo.Dto.RoleRequest;
import com.finalBanking.demo.Dto.RoleResponse;
import com.finalBanking.demo.Entity.Permission;
import com.finalBanking.demo.Entity.Role;
import com.finalBanking.demo.Repository.permissionRepository;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    private final roleRepository roleRepository;
    private final permissionRepository permissionRepository;


    public RoleServiceImpl(roleRepository roleRepository, permissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequestDTO) {
        Role role = new Role();
        role.setName(roleRequestDTO.getName());
        role.setDescription(roleRequestDTO.getDescription());
        role.setCreatedAt(LocalDateTime.parse(String.valueOf(LocalDateTime.now())));

        Role savedRole = roleRepository.save(role);

        return convertToRoleResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToRoleResponse)
                .collect(Collectors.toList());
    }



    @Override
    public RoleResponse assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.getPermissions().addAll(permissions);
        roleRepository.save(role);

        return convertToRoleResponse(role);
    }

    private RoleResponse convertToRoleResponse(Role role) {
        RoleResponse responseDTO = new RoleResponse();
        responseDTO.setId(role.getId());
        responseDTO.setName(role.getName());
        responseDTO.setDescription(role.getDescription());
        responseDTO.setCreatedAt(role.getCreatedAt());
        responseDTO.setPermissions(role.getPermissions().stream()
                .map(this::convertToPermissionResponse)
                .collect(Collectors.toSet()));
        return responseDTO;
    }

    private PermissionResponse convertToPermissionResponse(Permission permission) {
        PermissionResponse permissionDTO = new PermissionResponse();
        permissionDTO.setId(permission.getId());
        permissionDTO.setName(permission.getName());
        permissionDTO.setDescription(permission.getDescription());
        permissionDTO.setCategory(permission.getCategory());
        return permissionDTO;
    }
}
