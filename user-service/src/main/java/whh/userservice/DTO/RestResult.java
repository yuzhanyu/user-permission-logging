package whh.userservice.DTO;

import lombok.Data;

/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
@Data
public class RestResult<T> {
    /**
     * 业务返回码
     */
    private String code;

    /**
     * 业务提示信息
     */
    private String msg;

    /**
     * 业务数据
     */
    private T data;

    public RestResult() {
    }

    public RestResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RestResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
