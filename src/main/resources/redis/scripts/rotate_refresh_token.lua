-- KEYS
-- [1]: RT:{memberId}
-- [2]: RT:INDEX:{memberId}
-- [3]: RT:{memberId}:{token}
-- ARGV
-- [1]: token
-- [2]: refresh token entity
-- [3]: token expiry (sec)
-- [4]: stale status
-- [5]: grace expiry (sec)

local baseKey = KEYS[1]
local indexKey = KEYS[2]
local newTokenKey = KEYS[3]

local newToken = ARGV[1]
local entityJson = ARGV[2]
local ttl = tonumber(ARGV[3])
local stale = ARGV[4]
local graceTtl = tonumber(ARGV[5])

local tokens = redis.call('SMEMBERS', indexKey)

if #tokens > 0 then
    for _, token in ipairs(tokens) do
        local oldTokenKey = baseKey .. ':' .. token

        if redis.call('EXISTS', oldTokenKey) == 1 then
            local json = cjson.decode(redis.call('GET', oldTokenKey))
            json.status = stale
            redis.call('SET', oldTokenKey, cjson.encode(json), 'EX', graceTtl)
        else
            redis.call('SREM', indexKey, token)
        end
    end
end

redis.call('SET', newTokenKey, entityJson, 'EX', ttl)
redis.call('SADD', indexKey, newToken)

return true
