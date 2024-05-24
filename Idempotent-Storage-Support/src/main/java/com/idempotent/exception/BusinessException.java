package com.idempotent.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by BourneYeung on 2017/5/23.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {


    private static final long serialVersionUID = -4089196139261725270L;

    private int code;

    private String message;

    @JsonIgnore
    private String message_zh_CN;

    private String category;

    private String moreInfo;

    private Map<String, Object> errorParams;

    private BusinessExceptionLevel level;

    private String description;

    public BusinessException() {
        super();
    }

    public BusinessException(int code, String message) {
        super();
        this.setCode(code);
        this.setMessage(message);
    }

    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.code = 0;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = 0;
    }

    /**
     * 使用{@link MessageFormat} 对message进行占位符format
     *
     * @param code             错误code
     * @param message          错误信息
     * @param messageArguments 错误信息参数
     * @param moreInfo         更多错误信息
     */
    public BusinessException(int code, String message, String moreInfo, Object... messageArguments) {
        super();

        this.setCode(code);
        this.setMessage(MessageFormat.format(message, messageArguments));
        this.setMoreInfo(moreInfo);
    }

    /**
     * 使用{@link MessageFormat} 对message进行占位符format
     *
     * @param code             错误code
     * @param message          错误信息
     * @param messageArguments 错误信息参数
     */
    public BusinessException(int code, String message, Object... messageArguments) {
        super();

        this.setCode(code);
        this.setMessage(MessageFormat.format(message, messageArguments));
    }

    public BusinessException(int code, String message, String moreInfo) {
        super();

        this.setCode(code);
        this.setMessage(message);
        this.setMoreInfo(moreInfo);
    }

    public BusinessException(int code, String message, Map<String, Object> errorParams) {
        super();

        this.setCode(code);
        this.setMessage(message);
        this.setErrorParams(errorParams);

    }

    public BusinessException(int code, String category, String message, String message_zh_CN, BusinessExceptionLevel level, Map<String, Object> errorParams) {
        super();

        this.setCode(code);
        this.setCategory(category);
        this.setMessage(message);
        this.setMessage_zh_CN(message_zh_CN);
        this.setErrorParams(errorParams);
        this.setLevel(level);

    }

    public BusinessException(String category, String message, BusinessExceptionLevel level) {
        super();

        this.setCode(code);
        this.setCategory(category);
        this.setMessage(message);
        this.setLevel(level);

    }

    public BusinessException(int code, String category, String message, BusinessExceptionLevel level) {
        super();

        this.setCode(code);
        this.setCategory(category);
        this.setMessage(message);
        this.setLevel(level);

    }

    public BusinessException(int code, String message, String moreInfo, Map<String, Object> errorParams) {
        super();

        this.setCode(code);
        this.setMessage(message);
        this.setMoreInfo(moreInfo);
        this.setErrorParams(errorParams);
    }

    @JsonProperty
    public void setMessage_zh_CN(String message_zh_CN) {
        this.message_zh_CN = message_zh_CN;
    }

    private void formatErrorMessage() {

        Iterator<String> keyIterator = errorParams.keySet().iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            this.message = this.message.replace("{" + key + "}", errorParams.get(key).toString());
        }

    }


}
