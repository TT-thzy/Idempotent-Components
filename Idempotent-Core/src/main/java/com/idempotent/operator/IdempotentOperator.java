package com.idempotent.operator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idempotent.mongo.domain.MongoDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "idempotentoperators")
@Getter
@Setter
public class IdempotentOperator extends MongoDomain {

    private String token;

    @JsonIgnore
    private String result;

    @JsonIgnore
    private Object resultObj;

    private Status status;

    private Date updateTime;


    public static enum Status {
        INITIALIZED, PROCESSING, FINISHED
    }

    public boolean isExecuted() {
        return this.status.equals(Status.FINISHED);
    }

}
