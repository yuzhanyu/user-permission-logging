package whh.userservice.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import whh.userservice.Config.FeignClientConfig;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@FeignClient(
        name = "permission-service",
        url = "http://127.0.0.1:8082/permission",  // 从配置读取
        configuration = FeignClientConfig.class   // 应用拦截器配置
)
public interface UserClient {
    @PostMapping("/bind-default/{userId}")
    void bindDefaultRole(@PathVariable("userId") Long userId);

    @GetMapping("/role-code/{userId}")
    String getUserRoleCode(@PathVariable("userId") Long userId);

    @PutMapping("/upgrade-to-admin/{userId}")
    void upgradeToAdmin(@PathVariable("userId") Long userId);

    @PutMapping( "/downgrade-to-user/{userId}")
    void downgradeToUser(@PathVariable("userId") Long userId);

    @GetMapping("/userIds/{roleCode}")
    Long[] getUserIdsByRoleCode(@PathVariable("roleCode") String roleCode);
}
