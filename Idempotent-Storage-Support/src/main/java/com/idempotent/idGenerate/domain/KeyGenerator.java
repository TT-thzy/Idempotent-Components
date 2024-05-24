package com.idempotent.idGenerate.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Created by smart on 11/29/2017.
 */
@Data
@NoArgsConstructor
@Document(collection = "keygenerators")
public class KeyGenerator {

    private Long _id;

    private String name;

    private Long key;

}