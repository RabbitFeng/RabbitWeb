package per.rabbit.netty.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import per.rabbit.netty.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ChannelHandler.Sharable
public class GameServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private ApplicationContext applicationContext;

    private final Map<Integer, MethodWrapper> handlerMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initHandlers() {
        log.info("init GameServerHandler");
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Service.class);
        for (Object bean : beans.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(CmdHandler.class)) {
                    if (method.getReturnType() == String.class) {
                        log.info("valid: bean: {}, method: {}", bean.getClass().getName(), method.getName());
                    }
                    if (method.getParameterCount() > 1) {
                        log.error("parameter count is not support: {}", method.getParameterCount());
                        throw new RuntimeException("parameter count is not support! must be 1 or less");
                    }
                    if (method.getParameterCount() == 1 && method.getParameterTypes()[0] != CmdData.class) {
                        log.error("parameter type is not support: {}", method.getParameterCount());
                        throw new RuntimeException("parameter type must be CmdData");
                    }
                    CmdHandler annotation = method.getAnnotation(CmdHandler.class);
                    int cmd = annotation.value();
                    handlerMap.put(cmd, new MethodWrapper(bean, method));
                }
            }
        }
    }

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("新链接建立! {}", ctx.channel().id());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("链接 inactive! {}", ctx.channel().id());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("exceptionCaught: ", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.warn("心跳超时，强制关闭连接:{}", ctx.channel().id());
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String payload = msg.text();
        RequestPacket requestPacket = JSON.parseObject(payload, RequestPacket.class);

        // 2. 寻找匹配的cmd业务处理方法
        MethodWrapper methodWrapper = handlerMap.get(requestPacket.getCmd());
        if (methodWrapper != null) {
            log.info("find cmd: {}", requestPacket.getCmd());
            // 3. 执行业务方法
            Object object = methodWrapper.method.invoke(methodWrapper.bean,
                    new CmdData(ctx.channel(), requestPacket.getData()));
            if (object != null) {
                // 4. 返回消息
                ctx.channel().writeAndFlush(new TextWebSocketFrame(object.toString()));
            } else {
                log.warn("method return is null");
            }
        } else {
            log.warn("not find cmd: {}", requestPacket.getCmd());
        }

        log.info("客户端发送消息: {}", payload);
    }

    @AllArgsConstructor
    private static class MethodWrapper {
        Object bean;
        Method method;
    }
}
