package com.idempotent.mongo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Multilingual {

    @JsonProperty
    default String getMultilingualKey() {
        return this.getClass().getName();
    }

}
