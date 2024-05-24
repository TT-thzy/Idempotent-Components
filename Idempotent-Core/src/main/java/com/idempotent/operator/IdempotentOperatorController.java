package com.idempotent.operator;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@ResponseBody
@RequestMapping(value = "api/idempotentOperators", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdempotentOperatorController {

    private IdempotentOperatorManager idempotentOperatorManager;

    public IdempotentOperatorController(IdempotentOperatorManager idempotentOperatorManager) {
        this.idempotentOperatorManager = idempotentOperatorManager;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String get() {

        return idempotentOperatorManager.createIdempotentOperator().getToken();

    }

}
