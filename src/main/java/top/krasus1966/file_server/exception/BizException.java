package top.krasus1966.file_server.exception;


import org.springframework.http.HttpStatus;

/**
 * 通用业务异常
 *
 * @author Krasus1966
 * {@code {@code @date}} 2020/10/28 17:33
 **/
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 180318802880258762L;
    private final Integer code;

    public BizException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public BizException(String message) {
        super(message);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }


    public BizException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
