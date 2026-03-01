-- KEYS
-- [1]: RT:memberId
-- [2]: RT:INDEX:memberId
-- [3]: RT:memberId:token
-- ARGV
-- [1]: token
-- [2]: entityJson
-- [3]: ttl

local baseKey = KEYS[1]
local indexKey = KEYS[2]
local newTokenKey = KEYS[3]

local newToken = ARGV[1]
local entityJson = ARGV[2]
local ttl = tonumber(ARGV[3])

local tokens = redis.call('SMEMBERS', indexKey)

if #tokens > 0 then
    for _, v in ipairs(tokens) do
        redis.call('DEL', baseKey .. ":" .. v)
    end
end

redis.call('DEL', indexKey)
redis.call('SET', newTokenKey, entityJson, 'EX', ttl)
redis.call('SADD', indexKey, newToken)

return true