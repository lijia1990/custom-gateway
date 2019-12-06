--获取KEY
local key1 = KEYS[1]    --ip名称
local key2 = KEYS[2]    --接口路径

local limit_ip_count = tonumber( ARGV[1])
local limit_req_count = tonumber(ARGV[2])
local expire_time = tonumber(ARGV[3])



if limit_ip_count > 0 and key1 and ~key2 then
    local real_ip_count = tonumber(redis.call("get", key1))
    if real_ip_count and  real_ip_count >= limit_ip_count then
	     return false
	else
         redis.call("incr",key1)
         redis.call("expire",key1,expire_time)
         return true
    end
end

if limit_req_count > 0  and key2  then
  local real_req_count =  tonumber(redis.call("get", key2))
    if real_req_count and  real_req_count >= limit_req_count then
     return false
	else
	  redis.call("incr",key2)
      redis.call("expire",key2,expire_time)
      return true
    end
end