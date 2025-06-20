package whh.permissionservice.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import whh.permissionservice.Entity.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@Mapper
public interface UserRoleMapper {
    // 根据用户ID查询角色绑定信息
    Optional<UserRole> findByUserId(@Param("userId") Long userId);

    // 插入用户角色绑定
    void insertUserRole(UserRole userRole);

    // 更新用户角色绑定
    void updateUserRole(UserRole userRole);


    List<Long> findUserIdsByRoleId(Long roleId);
}
