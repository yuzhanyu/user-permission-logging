package whh.userservice.Utils;

/**
 * @author hanghang
 * @date 2025/6/21
 * @Description
 */

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import whh.userservice.Entity.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//AI辅助生成
@Component
public class LogUtils {
    private final JwtUtil jwtUtil;

    private final RocketMQTemplate rocketMQTemplate;

    public LogUtils(JwtUtil jwtUtil, RocketMQTemplate rocketMQTemplate) {
        this.jwtUtil = jwtUtil;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 记录用户操作日志（自动获取客户端IP）
     *
     * @param token  用户令牌（用于解析用户ID）
     * @param action 操作类型（如：登录、修改密码等）
     * @param detail 操作详情（自定义描述）
     */
    public void recordUserAction(String token, String action, String detail) {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("无法获取当前HTTP请求");
        }

        Map<String, Object> claims = jwtUtil.parseToken(token);
        Long userId = (Long) claims.get("userId");
        String clientIp = IpUtils.getClientIp(request);

        recordUserAction(userId, action, detail, clientIp);
    }

    /**
     * 记录用户操作日志（指定客户端IP）
     *
     * @param userId   用户ID
     * @param action   操作类型
     * @param detail   操作详情
     * @param clientIp 客户端IP
     */
    public void recordUserAction(Long userId, String action, String detail, String clientIp) {
        Log log = new Log();
        log.setUserId(userId);
        log.setAction(action);
        log.setIp(clientIp);
        log.setDetail(detail);

        sendToMq(log);
    }

    /**
     * 记录登录成功日志（简化方法）
     *
     * @param token 用户令牌
     */
    public void recordLoginSuccess(String token) {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("无法获取当前HTTP请求");
        }

        Map<String, Object> claims = jwtUtil.parseToken(token);
        Long userId = (Long) claims.get("userId");
        String clientIp = IpUtils.getClientIp(request);

        recordUserAction(
                userId,
                "登录操作",
                "用户 " + userId + " 在 " + clientIp + " 登录成功",
                clientIp
        );
    }

    /**
     * 记录操作失败日志（简化方法）
     *
     * @param token  用户令牌
     * @param action 操作类型
     * @param reason 失败原因
     */
    public void recordOperationFailure(String token, String action, String reason) {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("无法获取当前HTTP请求");
        }

        Map<String, Object> claims = jwtUtil.parseToken(token);
        Long userId = (Long) claims.get("userId");
        String clientIp = IpUtils.getClientIp(request);

        recordUserAction(
                userId,
                action + "失败",
                "用户 " + userId + " 执行 " + action + " 失败: " + reason,
                clientIp
        );
    }

    /**
     * 获取当前HTTP请求对象
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 发送日志到MQ
     */
    private void sendToMq(Log log) {
        rocketMQTemplate.send("log-topic", MessageBuilder.withPayload(log).build());
    }
}