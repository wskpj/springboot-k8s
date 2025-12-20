package com.example.springboot_app.domain.coupon.redis;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.example.springboot_app.global.redis.annotation.LuaExecute;
import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.dto.ValueBinding;
import com.example.springboot_app.global.redis.enums.RedisListKey;
import com.example.springboot_app.global.redis.enums.RedisLuaScript;
import com.example.springboot_app.global.redis.enums.RedisSetKey;
import com.example.springboot_app.global.redis.enums.RedisStringKey;
import com.example.springboot_app.global.redis.service.RedisListService;
import com.example.springboot_app.global.redis.service.RedisLuaScriptExecutor;
import com.example.springboot_app.global.redis.service.RedisSetService;
import com.example.springboot_app.global.redis.service.RedisStringService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponRedisRepository {

    private final RedisStringService redisStringService;
    private final RedisSetService redisSetService;
    private final RedisListService redisListService;
    private final RedisLuaScriptExecutor scriptExecutor;

    @LuaExecute
    public Long issueCoupon(Long couponId, Long userId) {
        // 1. Bind keys
        KeyBinding<RedisStringKey> stockKey = RedisStringKey.COUPON_STOCK.bind(couponId);
        KeyBinding<RedisSetKey> userSetKey = RedisSetKey.COUPON_ISSUED_USERS.bind(couponId);
        KeyBinding<RedisListKey> queueKey = RedisListKey.COUPON_ISSUE_QUEUE.bind();
        KeyBinding<RedisSetKey> syncKey = RedisSetKey.COUPON_SYNC_IDS.bind();

        // 2. Execute script
        return scriptExecutor.execute(
                RedisLuaScript.COUPON_ISSUE,
                List.of(stockKey, userSetKey, queueKey, syncKey),
                List.of(userId, couponId));
    }

    public boolean initializeStock(Long couponId, int quantity) {
        KeyBinding<RedisStringKey> key = RedisStringKey.COUPON_STOCK.bind(couponId);
        ValueBinding value = ValueBinding.of(String.valueOf(quantity));
        return redisStringService.setIfAbsent(key, value);
    }

    public void setStock(Long couponId, int quantity) {
        KeyBinding<RedisStringKey> key = RedisStringKey.COUPON_STOCK.bind(couponId);
        ValueBinding value = ValueBinding.of(String.valueOf(quantity));
        redisStringService.set(key, value);
    }

    public Integer getStock(Long couponId) {
        String stock = redisStringService.get(RedisStringKey.COUPON_STOCK.bind(couponId));
        return stock == null ? null : Integer.parseInt(stock);
    }

    public Long decreaseStock(Long couponId) {
        KeyBinding<RedisStringKey> key = RedisStringKey.COUPON_STOCK.bind(couponId);
        return redisStringService.decrement(key);
    }

    public void increaseStock(Long couponId) {
        KeyBinding<RedisStringKey> key = RedisStringKey.COUPON_STOCK.bind(couponId);
        redisStringService.increment(key);
    }

    public boolean isAlreadyIssued(Long couponId, Long userId) {
        KeyBinding<RedisSetKey> key = RedisSetKey.COUPON_ISSUED_USERS.bind(couponId);
        ValueBinding value = ValueBinding.of(String.valueOf(userId));
        return redisSetService.isMember(key, value);
    }

    public void addIssuedUser(Long couponId, Long userId) {
        KeyBinding<RedisSetKey> key = RedisSetKey.COUPON_ISSUED_USERS.bind(couponId);
        ValueBinding value = ValueBinding.of(String.valueOf(userId));
        redisSetService.add(key, value);
    }

    public void addIssueEvent(Long userId, Long couponId) {
        KeyBinding<RedisListKey> key = RedisListKey.COUPON_ISSUE_QUEUE.bind();
        ValueBinding value = ValueBinding.of(userId, couponId);
        redisListService.leftPush(key, value);
    }

    public void addSyncId(Long couponId) {
        KeyBinding<RedisSetKey> key = RedisSetKey.COUPON_SYNC_IDS.bind();
        ValueBinding value = ValueBinding.of(String.valueOf(couponId));
        redisSetService.add(key, value);
    }

    public Set<String> getAndClearSyncIds() {
        KeyBinding<RedisSetKey> key = RedisSetKey.COUPON_SYNC_IDS.bind();
        Set<String> members = redisSetService.members(key);
        if (members != null && !members.isEmpty()) {
            redisSetService.delete(key);
        }
        return members;
    }

    public List<String> getIssueEvents(int count) {
        KeyBinding<RedisListKey> key = RedisListKey.COUPON_ISSUE_QUEUE.bind();
        return redisListService.range(key, 0, count - 1);
    }

    public void trimIssueEvents(int count) {
        KeyBinding<RedisListKey> key = RedisListKey.COUPON_ISSUE_QUEUE.bind();
        redisListService.trim(key, count, -1);
    }
}
