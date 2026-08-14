package per.rabbit.netty;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局连接管理仓库
 * 在线的netty Channel交给Spring管理，方便在Channel和Service主动推送消息
 */
@Component
public class ChannelRepository {
    /**
     * 广播频道组
     */
    private final ChannelGroup globalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * userId -> Channel映射表
     */
    private final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public void add(String userId, Channel channel) {
        globalGroup.add(channel);
        userIdChannelMap.put(userId, channel);
    }

    public void removeChannel(Channel channel) {
        globalGroup.remove(channel);
        for (String s : userIdChannelMap.keySet()) {
            if (userIdChannelMap.get(s) == channel) {
                userIdChannelMap.remove(s);
            }
        }
    }

    public Channel get(String userId) {
        return userIdChannelMap.get(userId);
    }

    public ChannelGroup getChannelGroup() {
        return globalGroup;
    }

}
