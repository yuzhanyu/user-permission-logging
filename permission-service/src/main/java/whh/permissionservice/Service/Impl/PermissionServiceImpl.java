package whh.permissionservice.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whh.permissionservice.Entity.Role;
import whh.permissionservice.Entity.UserRole;
import whh.permissionservice.Mapper.RoleMapper;
import whh.permissionservice.Mapper.UserRoleMapper;
import whh.permissionservice.Service.PermissionService;

import java.util.Optional;

import static whh.permissionservice.Constant.ResultConstant.ROLE_ADMIN;
import static whh.permissionservice.Constant.ResultConstant.ROLE_USER;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public PermissionServiceImpl(UserRoleMapper userRoleMapper, RoleMapper roleMapper) {
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public void bindDefaultRole(Long userId) {
        // 1. 获取默认角色
        Role defaultRole = roleMapper.findByRoleCode(ROLE_USER)
                .orElseThrow(() -> new RuntimeException("默认角色不存在"));

        // 2. 检查是否已有角色绑定
        Optional<UserRole> existingBinding = userRoleMapper.findByUserId(userId);

        if (existingBinding.isPresent()) {
            // 3. 已存在绑定则更新
            UserRole userRole = existingBinding.get();
            userRole.setRoleId(defaultRole.getRoleId());
            userRoleMapper.updateUserRole(userRole);
        } else {
            // 4. 不存在则新建绑定
            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(userId);
            newUserRole.setRoleId(defaultRole.getRoleId());
            userRoleMapper.insertUserRole(newUserRole);
        }
    }

    @Override
    public String getUserRoleCode(Long userId) {
        // 1. 获取用户角色绑定
        UserRole userRole = userRoleMapper.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户角色未绑定"));

        // 2. 查询对应的角色信息
        return roleMapper.findById(userRole.getRoleId())
                .map(Role::getRoleCode)
                .orElseThrow(() -> new RuntimeException("角色信息不存在"));
    }

    @Override
    public void upgradeToAdmin(Long userId) {
        // 1. 校验用户ID有效性
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }

        // 2. 获取管理员角色
        Role adminRole = roleMapper.findByRoleCode(ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("管理员角色不存在"));

        // 3. 更新或创建角色绑定
        Optional<UserRole> existingBinding = userRoleMapper.findByUserId(userId);

        if (existingBinding.isPresent()) {
            UserRole userRole = existingBinding.get();
            userRole.setRoleId(adminRole.getRoleId());
            userRoleMapper.updateUserRole(userRole);
        } else {
            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(userId);
            newUserRole.setRoleId(adminRole.getRoleId());
            userRoleMapper.insertUserRole(newUserRole);
        }
    }

    @Override
    public void downgradeToUser(Long userId) {
        // 1. 获取普通用户角色
        Role userRole = roleMapper.findByRoleCode(ROLE_USER)
                .orElseThrow(() -> new RuntimeException("默认角色不存在"));

        // 2. 更新或创建角色绑定
        Optional<UserRole> existingBinding = userRoleMapper.findByUserId(userId);

        if (existingBinding.isPresent()) {
            UserRole binding = existingBinding.get();
            binding.setRoleId(userRole.getRoleId());
            userRoleMapper.updateUserRole(binding);
        } else {
            UserRole newBinding = new UserRole();
            newBinding.setUserId(userId);
            newBinding.setRoleId(userRole.getRoleId());
            userRoleMapper.insertUserRole(newBinding);
        }
    }
    @Override
    public Long[] getUserIdsByRoleCode(String roleCode) {
        // 1. 获取角色ID
        Role role = roleMapper.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        // 2. 获取用户角色绑定
        return userRoleMapper.findUserIdsByRoleId(Long.valueOf(role.getRoleId()))
                .stream()
                .map(Long.class::cast)
                .toArray(Long[]::new);
    }
}