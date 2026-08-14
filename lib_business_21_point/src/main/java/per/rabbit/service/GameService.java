package per.rabbit.service;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.rabbit.netty.ChannelRepository;
import per.rabbit.netty.handler.CmdHandler;
import per.rabbit.netty.handler.CmdData;

@Service
@Slf4j
public class GameService {
    @Autowired
    private ChannelRepository channelRepository;

    /**
     * 登录接口 10001
     * data:
     * {
     * "user_id": "xxx"
     * }
     *
     */
    @CmdHandler(10001)
    public long login(CmdData cmdData) {
        log.info("handleMsg, channel: {}, msg: {} {}", cmdData.getChannel(), cmdData.getMsg().getClass().getSimpleName(), cmdData.getMsg());
        if (cmdData.isJson()) {
            JSONObject json = cmdData.json();
            if (json != null) {
                String userId = json.getString("user_id");
                log.info("login, userId: {}, channel: {}", userId, cmdData.getChannel());
                if (userId != null) {
                    channelRepository.add(userId, cmdData.getChannel());
                }
            }
        }
        return 10001;
    }
}
