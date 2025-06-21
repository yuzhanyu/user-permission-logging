package whh.loggingservice.Repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import whh.loggingservice.Pojo.OperationLogs;

/**
 * @author hanghang
 * @date 2025/6/21
 * @Description
 */
@Mapper
public interface LogRepository {
    @Insert("insert into operation_logs(user_id,action,ip,detail) values(#{userId},#{action},#{ip},#{detail})")
    void save(OperationLogs log);
}
