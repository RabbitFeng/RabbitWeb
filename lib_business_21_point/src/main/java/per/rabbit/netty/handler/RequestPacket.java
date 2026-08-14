package per.rabbit.netty.handler;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestPacket {
    private int cmd;
    private String data; // 或者byte[] data 用于protobuf
}

