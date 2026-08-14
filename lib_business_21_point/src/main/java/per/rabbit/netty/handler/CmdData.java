package per.rabbit.netty.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CmdData {
    private Channel channel;
    private Object msg; // json 或者 纯文本

    public String text() {
        return msg.toString();
    }

    public JSONObject json() {
        if (isJson()) {
            return (JSONObject) msg;
        }
        return JSON.parseObject(msg.toString());
    }

    public boolean isJson() {
        return msg instanceof JSONObject;
    }
}
