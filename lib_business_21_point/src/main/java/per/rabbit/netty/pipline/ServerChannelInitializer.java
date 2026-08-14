package per.rabbit.netty.pipline;

import per.rabbit.config.NettyProperties;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import per.rabbit.netty.handler.GameServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private NettyProperties nettyProperties;

    @Autowired
    private GameServerHandler gameServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 1. 心跳检测，读超时
        pipeline.addLast(new IdleStateHandler(nettyProperties.getReaderIdleTime(),
                nettyProperties.getWriterIdleTime(),
                nettyProperties.getReaderIdleTime(),
                TimeUnit.SECONDS));

        // 2. Http解编码器 用于WebSocket握手阶段
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65535));

        // 3. WebSocket协议处理器（处理 Handshake ping pong close等）
        pipeline.addLast(new WebSocketServerProtocolHandler(nettyProperties.getPath(), null, true));

        // 4. 自定义业务处理器
        pipeline.addLast(gameServerHandler);

        // 5. 异常处理器
//        pipeline.addLast();
    }
}
