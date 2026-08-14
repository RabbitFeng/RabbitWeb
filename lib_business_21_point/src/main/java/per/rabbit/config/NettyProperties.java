package per.rabbit.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
public class NettyProperties {
    @Value("${netty.port}")
    private String port;
    @Value("${netty.path}")
    private String path;
    @Value("${netty.boss-thread-count}")
    private int bossThreads;
    @Value("${netty.worker-thread-count}")
    private int workerThreads;
    @Value("${netty.reader-idle-time}")
    private int readerIdleTime;
    @Value("${netty.writer-idle-time}")
    private int writerIdleTime;

}
