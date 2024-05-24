package com.idempotent.exception;

import java.util.List;

public interface CommonBusinessException {

    int getCode();

    String getCategory();

    String getMessage();

    String getMessage_zh_CN();

    BusinessExceptionLevel getLevel();

    List<BusinessExceptionParams> getParams();

    static CommonBusinessException defaultException(int exceptionCode) {
        return new CommonBusinessException() {

            private int code;

            private String message;

            private BusinessExceptionLevel level;

            {
                this.code = 70000;
                this.message = "找不到code为" + exceptionCode + "的BusinessException";
                this.level = BusinessExceptionLevel.WARNING;
            }

            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public BusinessExceptionLevel getLevel() {
                return BusinessExceptionLevel.WARNING;
            }

            @Override
            public List<BusinessExceptionParams> getParams() {
                return null;
            }

            @Override
            public String getCategory() {
                return null;
            }

            @Override
            public String getMessage_zh_CN() {
                return null;
            }
        };
    }
}
