package com.idempotent.exception;

import com.google.common.collect.ImmutableMap;
import com.idempotent.context.SpringApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;


/**
 * BusinessException构建器
 */
public class BusinessExceptionBuilder {

    /**
     * 使用错误code和错误内容参数进行抛错<br/>
     * 需要存在BusinessExceptionMessageFinder类型的bean<br/>
     *
     * @param code        错误code
     * @param errorParams 错误参数
     */
    public static BusinessException of(int code, Map<String, Object> errorParams) {


//        final BusinessExceptionMessageFinder businessExceptionMessageFinder = SpringApplicationContext.getBeanStatic(BusinessExceptionMessageFinder.class);

        //如果不是出于Spring容器环境  直接返回空的message信息
        if (SpringApplicationContext.getApplicationContextStatic() == null) {

            return BusinessExceptionBuilder.of(code, "", errorParams);

        }


        //处于容器环境 只有应用提供了自己的BusinessExceptionMessageFinder才进行message解析
        final ApplicationContext applicationContext = SpringApplicationContext.getApplicationContextStatic();

        final String[] beanNamesForType = applicationContext.getBeanNamesForType(BusinessExceptionMessageFinder.class);

        CommonBusinessException businessExceptionInfo = Stream.of(beanNamesForType)
                .findFirst()
                .map(beanNameForType -> ((BusinessExceptionMessageFinder) applicationContext.getBean(beanNameForType)))
                .map(businessExceptionMessageFinder -> businessExceptionMessageFinder.getBusinessExceptionInfo(code))
                .orElse(CommonBusinessException.defaultException(code));

        return BusinessExceptionBuilder.of(businessExceptionInfo, errorParams);

    }

    /**
     * 使用错误code和错误内容参数进行抛错<br/>
     * 需要存在BusinessExceptionMessageFinder类型的bean<br/>
     *
     * @param code        错误code
     */
    public static BusinessException of(int code) {
        return of(code, ImmutableMap.of());
    }


    /**
     * 使用code message和errorParams进行构建
     *
     * @param code
     * @param message
     * @param errorParams
     * @return
     */
    public static BusinessException of(int code, String message, Map<String, Object> errorParams) {

        //将message进行格式化
        String formatMsg = formatMessage(message, errorParams);

        return new BusinessException(code, formatMsg, errorParams);

    }

    /**
     * 使用BusinessExceptionInfo进行构建
     */
    public static BusinessException of(CommonBusinessException businessExceptionInfo, Map<String, Object> errorParams) {
        Integer code = businessExceptionInfo.getCode();
        String category = businessExceptionInfo.getCategory();
        String message = formatMessage(businessExceptionInfo.getMessage(), errorParams);
        String message_zh_CN = formatMessage(businessExceptionInfo.getMessage_zh_CN(), errorParams);
        BusinessExceptionLevel level = businessExceptionInfo.getLevel();

        return new BusinessException(code, category, message, message_zh_CN, level, errorParams);
    }

    /**
     * 将message进行格式化
     * @param message
     * @param errorParams
     * @return
     */
    private static String formatMessage(String message, Map<String, Object> errorParams) {
        if (StringUtils.isEmpty(message) || null == errorParams) {
            return message;
        }

        Iterator<String> keyIterator = errorParams.keySet().iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = (null == errorParams.get(key) ? "" : errorParams.get(key));
            message = message.replace("{" + key + "}", value.toString());
        }
        return message;
    }
}
