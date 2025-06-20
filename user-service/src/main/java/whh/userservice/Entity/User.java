package whh.userservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.sql.Timestamp;


/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Timestamp gmtCreate;
}
