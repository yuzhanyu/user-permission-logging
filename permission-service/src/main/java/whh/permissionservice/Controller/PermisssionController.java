package whh.permissionservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import whh.permissionservice.DTO.RestResult;
import whh.permissionservice.Service.PermissionService;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@RestController
@RequestMapping("/permission")
public class PermisssionController {
    @Autowired
    private PermissionService permissionService;
    /**
     * 绑定默认角色（普通用户）
     * 通常用于新用户注册时调用
     */
    @PostMapping("/bind-default/{userId}")
    public RestResult<Void> bindDefaultRole(@PathVariable Long userId) {
        permissionService.bindDefaultRole(userId);
        return RestResult.success("默认角色绑定成功");
    }

    /**
     * 查询用户角色码
     */
    @GetMapping("/role-code/{userId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or #userId == authentication.principal.id")
    public String getUserRoleCode(@PathVariable Long userId) {
        String roleCode = permissionService.getUserRoleCode(userId);
        return roleCode;
    }

    /**
     * 升级用户为管理员（仅限超管操作）
     */
    @PutMapping("/upgrade-to-admin/{userId}")
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public RestResult<Void> upgradeToAdmin(@PathVariable Long userId) {
        permissionService.upgradeToAdmin(userId);
        if (userId == null || userId <= 0) {
            return RestResult.fail("用户ID无效");
        }
        return RestResult.success("用户已升级为管理员");
    }

    /**
     * 降级用户为普通角色（仅限超管操作）
     */
    @PutMapping("/downgrade-to-user/{userId}")
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public RestResult<Void> downgradeToUser(@PathVariable Long userId) {
        permissionService.downgradeToUser(userId);
        return RestResult.success("用户已降级为普通角色");
    }
    /**
     * 获取用户ID列表
     */
    @GetMapping("/userIds/{roleCode}")
    public Long[] getUserIdsByRoleCode(@PathVariable String roleCode) {
        Long[] userIds = permissionService.getUserIdsByRoleCode(roleCode);
        return userIds;
    }
}