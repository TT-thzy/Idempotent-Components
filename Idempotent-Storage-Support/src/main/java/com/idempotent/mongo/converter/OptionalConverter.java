package com.idempotent.mongo.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Optional;

@ReadingConverter
public class OptionalConverter implements Converter<Document, Optional<Boolean>> {
    @Override
    public Optional<Boolean> convert(Document document) {
        return Optional.ofNullable((Boolean) document.get("value"));
    }
}
