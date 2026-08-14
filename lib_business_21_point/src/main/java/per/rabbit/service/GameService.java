package per.rabbit.service;

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
     * 处理1001的封装接口
     * @return
     */
    @CmdHandler(10001)
    public long handleMsg(CmdData cmdData) {
        log.info("handleMsg, channel: {}, msg: {}", cmdData.getChannel(), cmdData.getMsg());
        return 10001;
    }
}
