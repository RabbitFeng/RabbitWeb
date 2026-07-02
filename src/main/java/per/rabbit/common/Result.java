package per.rabbit.common;


public class Result<T> {
    private int errno;
    private String errmsg;

    private String msg;
    private String logid;
    private T data;

    private Result() {
    }

    public static <T> Result<T> success(T data) {
        return new Result<>() {
            {
                setErrno(0);
                setErrmsg("");
                setMsg("请求成功 Remote Map");
                setData(data);
                setLogid(String.valueOf(System.currentTimeMillis()));
            }
        };
    }

    public static <T> Result<T> failed(T data) {
        return new Result<>() {{
            setErrno(-1);
            setErrmsg("请求失败");
            setMsg("请求失败");
            setData(data);
            setLogid(String.valueOf(System.currentTimeMillis()));
        }};
    }

    @Override
    public String toString() {
        return "Result{" +
                "errno=" + errno +
                ", errmsg='" + errmsg + '\'' +
                ", msg='" + msg + '\'' +
                ", logid='" + logid + '\'' +
                ", data=" + data +
                '}';
    }

    public int getErrno() {
        return errno;
    }

    public Result<T> setErrno(int errno) {
        this.errno = errno;
        return this;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public Result<T> setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getLogid() {
        return logid;
    }

    public Result<T> setLogid(String logid) {
        this.logid = logid;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
