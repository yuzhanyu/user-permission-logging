package whh.loggingservice.Pojo;

import lombok.Data;

/**
 * @author hanghang
 * @date 2025/6/21
 * @Description 
 */
@Data
public class Event {
    private Long userId;
    private String action;
    private String ip;
    private String detail;
}
