package com.idempotent.mongo.converter;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;

@WritingConverter
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {
    @Override
    public Decimal128 convert(@NotNull BigDecimal bigDecimal) {
        return new Decimal128(bigDecimal.round(MathContext.DECIMAL128));
    }
}
