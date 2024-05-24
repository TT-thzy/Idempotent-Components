package com.idempotent.config;

import com.idempotent.mongo.converter.BigDecimalToDecimal128Converter;
import com.idempotent.mongo.converter.Decimal128ToBigDecimalConverter;
import com.idempotent.mongo.converter.DocumentToMapConverter;
import com.idempotent.mongo.converter.OptionalConverter;
import com.idempotent.mongo.driver.HybridMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CustomMongoConfiguration {

    @Autowired
    private Environment environment;

    @Primary
    @Bean
    public HybridMongoTemplate hybridMongoTemplate(MongoDatabaseFactory mongoDatabaseFactory,
                                                   MongoConverter converter) {

        if (converter instanceof MappingMongoConverter) {
            MappingMongoConverter mappingMongoConverter = (MappingMongoConverter) converter;
            List<Converter<?, ?>> converters = Arrays.asList(new DocumentToMapConverter(), new OptionalConverter(), new BigDecimalToDecimal128Converter(), new Decimal128ToBigDecimalConverter());
            final MongoCustomConversions mongoCustomConversions = new MongoCustomConversions(converters);
            mappingMongoConverter.setCustomConversions(mongoCustomConversions);
            ((MappingMongoConverter) converter).afterPropertiesSet();
        }

        return new HybridMongoTemplate(mongoDatabaseFactory, converter);
    }

}
