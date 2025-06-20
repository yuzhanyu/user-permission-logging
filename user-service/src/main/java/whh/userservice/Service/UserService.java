package whh.userservice.Service;

import com.github.pagehelper.PageInfo;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Entity.User;

/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
public interface UserService {

    Boolean registerUser(UserDTO userDtO);
    String loginUser(UserDTO userDtO);
    PageInfo<User> findUsers(UserDTO userDtO);
    User getUser(UserDTO userDtO,Long userId);
    Boolean updateUser(Long userId, UserDTO userDtO);
    Boolean resetPassword(Long userId, UserDTO userDtO);
}
