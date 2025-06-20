package whh.permissionservice.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import whh.permissionservice.Entity.Role;

import java.util.Optional;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@Mapper
public interface RoleMapper {
    // 根据角色码查询角色
    Optional<Role> findByRoleCode(@Param("roleCode") String roleCode);

    // 根据角色ID查询角色
    Optional<Role> findById(@Param("roleId") Integer roleId);


}
