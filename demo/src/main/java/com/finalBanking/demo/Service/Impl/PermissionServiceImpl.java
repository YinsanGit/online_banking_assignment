package com.finalBanking.demo.Service.Impl;

import com.finalBanking.demo.Dto.PermissionRequest;
import com.finalBanking.demo.Dto.PermissionResponse;
import com.finalBanking.demo.Entity.Permission;
import com.finalBanking.demo.Repository.permissionRepository;
import com.finalBanking.demo.Service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PermissionServiceImpl implements PermissionService {

    private permissionRepository permissionRepository;

    public PermissionServiceImpl(permissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequestDTO) {
        Permission permission = new Permission();
        permission.setName(permissionRequestDTO.getName());
        permission.setDescription(permissionRequestDTO.getDescription());
        permission.setCategory(permissionRequestDTO.getCategory());

        Permission savedPermission = permissionRepository.save(permission);

        return convertToPermissionResponse(savedPermission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::convertToPermissionResponse)
                .collect(Collectors.toList());
    }

    private PermissionResponse convertToPermissionResponse(Permission permission) {
        PermissionResponse permissionDto = new PermissionResponse();
        permissionDto.setId(permission.getId());
        permissionDto.setName(permission.getName());
        permissionDto.setDescription(permission.getDescription());
        permissionDto.setCategory(permission.getCategory());
        return permissionDto;
    }
}
