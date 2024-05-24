package com.idempotent.mongo.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Smart Lee 2020/3/24 11:49
 */
@ReadingConverter
public class DocumentToMapConverter implements Converter<Document, Map<String, Object>> {


    @Override
    public Map<String, Object> convert(Document source) {
        if (source == null) return null;
        final HashMap<String, Object> result = new HashMap<>();
        result.putAll(source);
        return result;
    }
}
