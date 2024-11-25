package com.uydev.services;

import com.uydev.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();

    List<RoleDTO> getAllRoleForRoot();

    RoleDTO findById(long roleId);

    List<RoleDTO> getAllRolesForCreateUser();
}
