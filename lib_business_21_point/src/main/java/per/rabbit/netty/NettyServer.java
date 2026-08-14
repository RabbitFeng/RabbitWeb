package per.rabbit.netty;

import per.rabbit.config.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import per.rabbit.netty.pipline.ServerChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyServer implements CommandLineRunner {

    @Autowired
    private NettyProperties nettyProperties;
    @Autowired
    private ServerChannelInitializer channelInitializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public void run(String... args) throws Exception {
        log.info("Netty服务启动...");
        // 后台线程启动netty，避免阻塞Spring容器主线程
        new Thread(() -> {
            try {
                this.start();
            } catch (Exception e) {
                log.error("Netty启动失败", e);
            }
        }).start();
    }

    public void start() throws InterruptedException {
        boolean useEpoll = Epoll.isAvailable();
        bossGroup = useEpoll ? new EpollEventLoopGroup(nettyProperties.getBossThreads())
                : new NioEventLoopGroup(nettyProperties.getBossThreads());
        workerGroup = useEpoll ? new EpollEventLoopGroup(nettyProperties.getWorkerThreads())
                : new NioEventLoopGroup(nettyProperties.getWorkerThreads());

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(useEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            log.info("Netty WebSocket 服务正在启动，监听端口: {}, 路径: {}",
                    nettyProperties.getPort(), nettyProperties.getPath());

            ChannelFuture future = b.bind(Integer.parseInt(nettyProperties.getPort())).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Netty 服务运行中断", e);
            Thread.currentThread().interrupt();
        } finally {
            destroy();
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("正在优雅关闭 Netty 服务...");
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
    }
}
