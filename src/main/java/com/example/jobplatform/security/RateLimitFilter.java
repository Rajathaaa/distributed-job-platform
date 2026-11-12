package com.example.jobplatform.security;
import com.example.jobplatform.service.RateLimitService; import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter; import java.io.IOException;
@Component public class RateLimitFilter extends OncePerRequestFilter {
 private final RateLimitService limiter; public RateLimitFilter(RateLimitService l){limiter=l;}
 @Override protected boolean shouldNotFilter(HttpServletRequest r){return r.getRequestURI().startsWith("/actuator")||r.getRequestURI().startsWith("/api/auth");}
 @Override protected void doFilterInternal(HttpServletRequest r,HttpServletResponse s,FilterChain c)throws ServletException,IOException{String key=r.getRemoteAddr();if(!limiter.allow(key,60)){s.setStatus(429);s.setContentType("application/json");s.getWriter().write("{\"error\":\"rate limit exceeded\"}");return;}c.doFilter(r,s);}
}
