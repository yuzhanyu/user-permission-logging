package whh.permissionservice.DTO;

import lombok.Data;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@Data
public class RestResult<T> {
    private int code;
    private String msg;
    private T data;
    
    public RestResult() {
    }
    public RestResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public RestResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> RestResult<T> success(T data) {
        return new RestResult<>(200, "操作成功", data);
    }

    public static <T> RestResult<T> success(String message) {
        return new RestResult<>(200, message, null);
    }

    public static RestResult<Void> fail(String message) {
        return new RestResult<>(500, message, null);
    }
}
