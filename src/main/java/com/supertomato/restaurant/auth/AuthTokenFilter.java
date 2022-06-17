package com.supertomato.restaurant.auth;

import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DiGiEx
 */
@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    AuthHelper authHelper;

    /**
     * Do filter all request, if any request don't have token => though error =>
     * Will enable it later when all API apply securejson web token rest api spring
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authToken = request.getHeader(Constant.HEADER_TOKEN);
        if (authToken != null) {
            try {
                AuthUser authUser = authHelper.loadAuthUserFromAuthToken(authToken);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ApplicationException ex) {
                // token not found or expired
                log.debug("doFilterInternal", ex);
            }
        }

        // Allow cross domain
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        // Allow set custom header token
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header " + Constant.HEADER_TOKEN);

        chain.doFilter(request, response);
    }
}
