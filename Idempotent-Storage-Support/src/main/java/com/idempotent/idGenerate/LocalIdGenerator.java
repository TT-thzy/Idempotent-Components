package com.idempotent.idGenerate;

import com.idempotent.exception.BusinessException;
import com.idempotent.idGenerate.domain.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class LocalIdGenerator implements IdGenerator{

    @Autowired
    private MongoTemplate mongoTemplate;

    public Long generateKeyDB() {
        final KeyGenerator keyGenerator = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(1L)),
                new Update().inc("key", 1L),
                KeyGenerator.class);
        if (keyGenerator == null) {
            throw new BusinessException(0, "更新Id生成器的key失败,操作失败！");
        }
        return keyGenerator.getKey();
    }

    @Override
    public Long generateId() {
        return this.generateKeyDB();
    }
}