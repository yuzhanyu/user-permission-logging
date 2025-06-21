package whh.loggingservice.Service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import whh.loggingservice.Pojo.Event;
import whh.loggingservice.Pojo.OperationLogs;
import whh.loggingservice.Repository.LogRepository;


/**
 * @author hanghang
 * @date 2025/6/21
 * @Description
 */
@Component
@RocketMQMessageListener(topic = "log-topic", consumerGroup = "log-consumer-group")
public class RocketMqService implements RocketMQListener<Event> {
    @Autowired
    private LogRepository logRepository;

    @Override
    public void onMessage(Event event) {
        OperationLogs log= new OperationLogs();
        log.setUserId(event.getUserId());
        log.setAction(event.getAction());
        log.setIp(event.getIp());
        log.setDetail(event.getDetail());
        logRepository.save(log);
    }
}