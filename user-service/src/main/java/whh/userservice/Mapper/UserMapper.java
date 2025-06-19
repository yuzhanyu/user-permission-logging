package whh.userservice.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import whh.userservice.Entity.User;

import java.util.Map;

/**
 * @author hanghang
 * @date 2025/6/18
 * @Description
 */
@Mapper
public interface UserMapper {

    void registerUser(Map<String, Object> params);
    User findByUsername(String name);
    User findByUsernameAndPwd(@Param("name") String name, @Param("password") String password);
}
