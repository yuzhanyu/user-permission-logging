package whh.userservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Table;


/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
@Data
@AllArgsConstructor
@Table(name = "users")
public class User {
    private Integer userId;
    private String name;
    private String password;
    private String email;
    private String phone;
    private Data gmtCreate;
}
