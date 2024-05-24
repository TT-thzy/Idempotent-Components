package com.idempotent.operator;

import com.idempotent.idGenerate.IdGenerator;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 幂等操作key管理器
 */
public class IdempotentOperatorManager {


    private MongoTemplate mongoTemplate;

    private IdGenerator idGenerator;

    private int processTimeoutMinutes;

    public IdempotentOperatorManager(MongoTemplate mongoTemplate, IdGenerator idGenerator, int processTimeoutMinutes) {

        this.mongoTemplate = mongoTemplate;
        this.idGenerator = idGenerator;
        this.processTimeoutMinutes = processTimeoutMinutes;

    }

    public IdempotentOperator createIdempotentOperator() {

        IdempotentOperator idempotentOperator = new IdempotentOperator();
        idempotentOperator.setId(idGenerator.generateId().toString());
        idempotentOperator.setResult(null);
        idempotentOperator.setToken(UUID.randomUUID().toString());
        idempotentOperator.setStatus(IdempotentOperator.Status.INITIALIZED);
        idempotentOperator.setUpdateTime(new Date());
        mongoTemplate.save(idempotentOperator);

        return idempotentOperator;

    }

    public Optional<IdempotentOperator> findById(String id) {

        return Optional.ofNullable(mongoTemplate.findById(id, IdempotentOperator.class));

    }

    public Optional<IdempotentOperator> findByKey(String token) {
        return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("token").is(token)), IdempotentOperator.class));
    }


    public boolean processing(String id) {

        final Criteria idAndStatus = Criteria.where("_id").is(id).and("status").is(IdempotentOperator.Status.INITIALIZED);
        final Instant earlyValidInstant = Instant.now().minus(Duration.ofMinutes(processTimeoutMinutes));
        final Criteria timeoutProcessing = Criteria.where("updateTime").lt(Date.from(earlyValidInstant)).and("status").is(IdempotentOperator.Status.PROCESSING).and("_id").is(id);
        final Query query = Query.query(new Criteria().orOperator(idAndStatus, timeoutProcessing));

        final UpdateResult updateResult = mongoTemplate.updateFirst(
                query,
                Update.update("status", IdempotentOperator.Status.PROCESSING).set("updateTime", new Date()), IdempotentOperator.class);

        return updateResult.getMatchedCount() > 0;

    }

    public boolean complete(String id, Object result) {

        final Criteria idAndStatus = Criteria.where("_id").is(id).and("status").is(IdempotentOperator.Status.PROCESSING);

        final UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(idAndStatus),
                Update.update("status", IdempotentOperator.Status.FINISHED).set("resultObj", result).set("updateTime", new Date()), IdempotentOperator.class);

        return updateResult.getModifiedCount() > 0;

    }

    public boolean fail(String id) {
        final Criteria idAndStatus = Criteria.where("_id").is(id).and("status").is(IdempotentOperator.Status.PROCESSING);
        final Query query = Query.query(idAndStatus);
        final UpdateResult updateResult = mongoTemplate.updateFirst(
                query,
                Update.update("status", IdempotentOperator.Status.INITIALIZED).set("updateTime", new Date()), IdempotentOperator.class);
        return updateResult.getModifiedCount() > 0;
    }


}
