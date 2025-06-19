package whh.userservice.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanghang
 * @date 2025/6/18
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDo {
    private String name;
    private String password;
}
