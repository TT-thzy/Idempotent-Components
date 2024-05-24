package com.idempotent.exception;

import lombok.Data;

/**
 * @author Michael
 * @date 2018-09-03
 */
@Data
public class BusinessExceptionParams {

    private String name;

    private String description;
}
