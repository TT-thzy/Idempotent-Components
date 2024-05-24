package com.idempotent.mongo.manager;

import com.idempotent.exception.BusinessException;
import com.idempotent.exception.BusinessExceptionLevel;
import com.idempotent.idGenerate.IdGenerator;
import com.idempotent.mongo.domain.LongMongoDomain;
import com.idempotent.mongo.domain.MongoDomain;
import com.idempotent.mongo.driver.HybridMongoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;


public abstract class AbstractLongMongoCurdManager<T extends LongMongoDomain, PK extends Long> implements CurdManager<T, PK> {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HybridMongoTemplate mongoTemplate;

    protected IdGenerator idgenerator;

    protected Class<T> entityType;


    public AbstractLongMongoCurdManager(HybridMongoTemplate mongoTemplate, IdGenerator idgenerator) {

        this.mongoTemplate = mongoTemplate;
        this.idgenerator = idgenerator;
        this.entityType = ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    /**
     * 新建
     *
     * @param t
     * @return
     */
    public T saveNew(T t) {

        //如果id不存在
        if (Objects.isNull(t.getId())) {
            t.setId(idgenerator.generateId());
        }

        mongoTemplate.save(t);

        return t;
    }


    /**
     * 更新
     *
     * @param t
     * @return
     */
    public T saveUpdate(T t, PK id) {
        t.setId(id);
        mongoTemplate.save(t);

        return t;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public void delete(PK id) {

        mongoTemplate.remove(Query.query(Criteria.where(MongoDomain.idProperty).is(id)), entityType);

    }

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    public T findById(PK id) {
        T t = mongoTemplate.findById(id, entityType);
        if (t == null) {
            throw new BusinessException(entityType.getSimpleName(),
                    entityType.getSimpleName() + " '" + id + "' is not exists!", BusinessExceptionLevel.DEBUG);
        }
        return t;

    }

    public Page<T> findAll(Query query, Pageable pageable) {

        return this.findAll(query, pageable, entityType);

    }

    /**
     * 根据条件查找分页数据
     *
     * @param query      query
     * @param pageable   分页信息
     * @param entityType 查找类型
     * @return
     */
    public <E> Page<E> findAll(Query query, Pageable pageable, Class<E> entityType) {

        long total = mongoTemplate.count(query, entityType);

        query.with(pageable);
        List<E> content = mongoTemplate.find(query, entityType);

        PageImpl<E> page = new PageImpl<E>(content, pageable, total);

        return page;

    }
}
