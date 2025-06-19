package whh.userservice.Service;

import whh.userservice.DTO.UserDTO;

/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
public interface UserService {

    Boolean registerUser(UserDTO userDtO);
    String loginUser(UserDTO userDtO);

}
