package whh.permissionservice.Service;

import org.springframework.stereotype.Service;

/**
 * @author hanghang
 * @date 2025/6/18
 * @Description
 */
@Service
// RPC接口定义
public interface PermissionService {
    // 绑定默认角色（普通用户）
    void bindDefaultRole(Long userId);

    // 查询用户角色码（返回role_code）
    String getUserRoleCode(Long userId);

    // 超管调用：升级用户为管理员
    void upgradeToAdmin(Long userId);

    // 超管调用：降级用户为普通角色
    void downgradeToUser(Long userId);

    // 获取用户角色码对应的用户ID列表
    Long[] getUserIdsByRoleCode(String roleCode);
}