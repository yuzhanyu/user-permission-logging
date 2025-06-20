package whh.userservice.Mapper;

import org.apache.ibatis.annotations.Mapper;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Entity.User;

import java.util.List;
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
    List<User> findAllUsers();
    List<User> findUsers(UserDTO userDTO);
    User findById(Long userDTO);
}
