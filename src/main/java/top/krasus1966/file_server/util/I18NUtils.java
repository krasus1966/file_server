package top.krasus1966.file_server.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Krasus1966
 * @date 2023/4/3 23:49
 **/
public class I18NUtils {
    private static final MessageSource messageSource = SpringUtil.getBean(MessageSource.class);

    public static String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
