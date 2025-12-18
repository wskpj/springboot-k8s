-- rate_limit.lua
-- 설명: INCR, EXPIRE 연산을 Atomic하게 처리하는 Lua 스크립트

-- KEYS[1]: Redis Key (String)
-- ARGV[1]: Limit count

local count = redis.call('INCR', KEYS[1])
if count == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[1])
end
return count
