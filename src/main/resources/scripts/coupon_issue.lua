-- coupon_issue.lua
-- 설명: 쿠폰 발급 로직 (Atomicity)

-- KEYS[1]: stockKey (String)
-- KEYS[2]: userSetKey (Set)
-- KEYS[3]: queueKey (List)
-- KEYS[4]: syncKey (Set)
-- ARGV[1]: userId
-- ARGV[2]: couponId

-- 1. Check if user already has it
if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
    return -101 -- Already Issued
end

-- 2. Check stock
local stock = tonumber(redis.call('GET', KEYS[1]))
if not stock or stock <= 0 then
    return -102 -- Out of Stock
end

-- 3. Execute atomic update
redis.call('DECR', KEYS[1])
redis.call('SADD', KEYS[2], ARGV[1])
redis.call('LPUSH', KEYS[3], ARGV[1] .. ":" .. ARGV[2])
redis.call('SADD', KEYS[4], ARGV[2])

return 0 -- Success
