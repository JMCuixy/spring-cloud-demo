package com.cloud.demo.stream.config;

import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 分区选择计算规则为 hashCode(key) % partitionCount , 这里的 key 根据 partitionKeyExpression 或 partitionKeyExtractorName 的配置计算得到
 * @author : cuixiuyin
 * @date : 2019/8/7
 */
@Component
public class KeyStrategy implements PartitionKeyExtractorStrategy {

    @Override
    public Object extractKey(Message<?> message) {
        return message.getPayload();
    }
}
