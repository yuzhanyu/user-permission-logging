package whh.userservice.Exception;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
public class PermissionServiceException extends RuntimeException {
    public PermissionServiceException(String message) {
        super("权限服务异常: " + message);
    }
}