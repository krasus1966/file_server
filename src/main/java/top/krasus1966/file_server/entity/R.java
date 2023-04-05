package top.krasus1966.file_server.entity;


import org.springframework.http.HttpStatus;
import top.krasus1966.file_server.util.I18NUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用返回封装
 *
 * @author Krasus1966
 * {@code @date} 2020/10/8 21:42
 **/
public class R<T> implements Serializable {
    private static final long serialVersionUID = 9140215144638597826L;

    private Integer code;
    private String msg;
    private T data;

    private LocalDateTime timestamp = LocalDateTime.now();


    private R() {
    }

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功
     *
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * {@code @date} 2022/10/31 11:14
     * @description 成功
     */
    public static <T> R<T> success() {
        return parse(HttpStatus.OK.value(), I18NUtils.getMessage("action.success"), null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * {@code @date} 2022/10/31 11:00
     * @description 成功
     */
    public static <T> R<T> success(T data) {
        return parse(HttpStatus.OK.value(), I18NUtils.getMessage("action.success"), data);
    }

    /**
     * 成功
     *
     * @param msg  消息
     * @param data 数据
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * {@code @date} 2022/10/31 11:01
     * @description 成功
     */
    public static <T> R<T> success(String msg, T data) {
        return parse(HttpStatus.OK.value(), msg, data);
    }

    /**
     * 失败
     *
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method failed
     * @author krasus1966
     * {@code @date} 2022/10/31 11:01
     * @description 失败
     */
    public static <T> R<T> failed() {
        return parse(HttpStatus.INTERNAL_SERVER_ERROR.value(), I18NUtils.getMessage("action.failed"), null);
    }

    /**
     * 失败
     *
     * @param msg 消息
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method failed
     * @author krasus1966
     * {@code @date} 2022/10/31 11:01
     * @description 失败
     */
    public static <T> R<T> failed(String msg) {
        return parse(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }



    /**
     * 通用返回
     *
     * @param code code
     * @param msg  消息
     * @param data 数据对象
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method parse
     * @author krasus1966
     * {@code @date} 2022/10/31 10:57
     * @description 通用返回
     */
    public static <T> R<T> parse(Integer code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    public boolean isSuccessful() {
        return this.code == HttpStatus.OK.value();
    }

    public Integer getCode() {
        return code;
    }

    public R<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public R<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public R<T> setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
