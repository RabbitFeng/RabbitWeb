package per.rabbit.netty.handler;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestPacket {
    private int cmd;
    private Object data;
}

