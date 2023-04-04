package top.krasus1966.file_server.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.krasus1966.file_server.entity.R;
import top.krasus1966.file_server.exception.BizException;
import top.krasus1966.file_server.util.I18NUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 通用自定义异常处理
 *
 * @author Krasus1966
 * {@code @date} 2020/10/28 18:03
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     *
     * @param e        自定义异常
     * @param response 响应
     * @return top.krasus1966.base.result.R<java.lang.String>
     * @method errorHandler
     * @author krasus1966
     * {@code @date} 2022/1/4 22:03
     * @description 自定义异常处理
     */
    @ExceptionHandler(value = {BizException.class})
    public R<String> errorHandler(BizException e, HttpServletResponse response) {
        String message = e.getMessage();
        Integer code = e.getCode();
        response.setStatus(code);
        return R.parse(code, message, null);
    }

    /**
     * 未知异常处理
     *
     * @param e 异常
     * @return top.krasus1966.base_project.common.core.entity.R
     * @method commonErrorHandler
     * @author krasus1966
     * {@code @date} 2022/11/2 15:19
     * @description 未知异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> commonErrorHandler(Exception e) {
        log.error("服务器内部异常，详细日志:", e);
        return R.failed(I18NUtils.getMessage("exception.internal_server_error"));
    }
}
