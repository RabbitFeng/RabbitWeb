package per.rabbit.common;


public class Result<T> {
    private int code;
    private String msg;
    private String logId;
    private T data;

    private Result() {
    }

    public static <T> Result<T> success(T data) {
        return new Result<>() {
            {
                setCode(0);
                setMsg("请求成功");
                setData(data);
                setLogId(String.valueOf(System.currentTimeMillis()));
            }
        };
    }

    public static <T> Result<T> failed(T data) {
        return new Result<>() {{
            setCode(-1);
            setMsg("请求失败");
            setData(data);
            setLogId(String.valueOf(System.currentTimeMillis()));
        }};
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", logid=" + logId +
                ", data=" + data +
                '}';
    }
}
