package whh.userservice.DTO;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author hanghang
 * @date 2025/6/18
 * @Description
 */
@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Timestamp gmtCreate;
    private Integer PageNum;
    private Integer PageSize;
    private List<Long> userIds;
    private String token;
    private String roleCode;

}
