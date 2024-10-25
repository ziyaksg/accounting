package com.uydev.services.impl;

import com.uydev.dto.RoleDTO;
import com.uydev.entity.Role;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.RoleRepository;
import com.uydev.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final MapperUtil mapper;

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> allRoles =  repository.findAll();
        return  allRoles.stream().map(role->mapper.convert(role,new RoleDTO())).toList();
    }

    @Override
    public List<RoleDTO> getAllRoleForRoot() {
        return List.of(mapper.convert(repository.findById(2L),new RoleDTO()));
    }

    @Override
    public RoleDTO findById(long roleId) {
       return mapper.convert(repository.findById(roleId), new RoleDTO());
    }
}
