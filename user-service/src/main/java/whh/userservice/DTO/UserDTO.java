package whh.userservice.DTO;

import lombok.Data;

/**
 * @author hanghang
 * @date 2025/6/18
 * @Description
 */
@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
}
