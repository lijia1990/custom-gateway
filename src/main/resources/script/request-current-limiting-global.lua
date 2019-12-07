--获取KEY
local key1 = KEYS[1]    --ip名称
local key2 = KEYS[2]    --接口路径

local limit_ip_count = tonumber( ARGV[1])
local limit_req_count = tonumber(ARGV[2])
local ip_expire_time = tonumber(ARGV[3])
local req_expire_time = tonumber(ARGV[4])


--只存在全局IP限流 不存在请求限流的情况下
if limit_ip_count and limit_ip_count >= 0  and  limit_req_count and  limit_req_count < 0 then
    local real_ip_count = tonumber(redis.call("get", key1))
    if real_ip_count and  real_ip_count >= limit_ip_count then
	     return false
	else
         redis.call("incr",key1)
         redis.call("expire",key1,ip_expire_time)
         return true
    end
end

--存在全局IP限流及请求限流
if limit_ip_count and limit_ip_count >= 0 and limit_req_count and limit_req_count >= 0 then

    local real_ip_count  =  tonumber(redis.call("get", key1))
    local real_req_count =  tonumber(redis.call("get", key2))

    if real_ip_count and  real_ip_count >= limit_ip_count then
	     return false
	end

	redis.call("incr",key1)
    redis.call("expire",key1,ip_expire_time)

	if real_req_count and  real_req_count >= limit_req_count then
    	     return false
	else
         redis.call("incr",key2)
         redis.call("expire",key2,req_expire_time)
         return true
    end
end

--只存在请求限流

if limit_ip_count and limit_ip_count < 0 and limit_req_count and limit_req_count >= 0 then

   local real_req_count =  tonumber(redis.call("get", key2))

	if real_req_count and  real_req_count >= limit_req_count then
    	     return false
	else
         redis.call("incr",key2)
         redis.call("expire",key2,req_expire_time)
         return true
    end

end