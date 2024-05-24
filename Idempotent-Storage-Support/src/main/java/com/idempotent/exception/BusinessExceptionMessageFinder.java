package com.idempotent.exception;

/**
 * 业务错误信息查找器<br>
 * 应用通过提供自己的Finder来自定义存储方式<br>
 *
 */
public interface BusinessExceptionMessageFinder {

    /**
     * 使用code查找相应的信息
     *
     * @param code
     * @return
     */
    CommonBusinessException getBusinessExceptionInfo(int code);

}
