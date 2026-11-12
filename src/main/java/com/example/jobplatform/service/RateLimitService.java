package com.example.jobplatform.service;
import org.springframework.data.redis.core.StringRedisTemplate; import org.springframework.stereotype.Service; import java.time.Duration;
@Service public class RateLimitService {
 private final StringRedisTemplate redis; public RateLimitService(StringRedisTemplate redis){this.redis=redis;}
 public boolean allow(String key,int limit){String k="rate:"+key+":"+(System.currentTimeMillis()/60000);Long n=redis.opsForValue().increment(k);if(n!=null&&n==1)redis.expire(k,Duration.ofMinutes(2));return n==null||n<=limit;}
}
