package com.idempotent.mongo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "clazz", visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MongoDomain implements Serializable, Multilingual {

    public static final String idProperty = "_id";

    @Id
    protected String _id;

    protected String idReference;

    protected String clazz;

    @CreatedDate
    protected Date createdDate;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    protected Date modifiedDate;

    @LastModifiedBy
    protected String modifiedBy;

//    @Version
//    private Long versionNumber;

    @JsonIgnore
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getClazz() {
        return this.getClass().getName();
    }

}
