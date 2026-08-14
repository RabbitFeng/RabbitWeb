package per.rabbit.netty.handler;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CmdData {
    private Channel channel;
    private String msg;
}
