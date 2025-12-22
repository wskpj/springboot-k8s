package com.example.springboot_app.domain.coupon.redis;

import java.util.List;
import java.util.Set;

public interface CouponRedisRepository {

    Long issueCoupon(Long couponId, Long userId);

    boolean initializeStock(Long couponId, int quantity);

    void setStock(Long couponId, int quantity);

    Integer getStock(Long couponId);

    Long decreaseStock(Long couponId);

    void increaseStock(Long couponId);

    boolean isAlreadyIssued(Long couponId, Long userId);

    void addIssuedUser(Long couponId, Long userId);

    void addIssueEvent(Long userId, Long couponId);

    void addSyncId(Long couponId);

    Set<String> getAndClearSyncIds();

    List<String> getIssueEvents(int count);

    void trimIssueEvents(int count);
}
