package com.mastersessay.blockchain.accounting.configuration.security;

import com.mastersessay.blockchain.accounting.service.BlockchainAccountingUserDetailsService;
import com.mastersessay.blockchain.accounting.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.AUTHORIZATION_HEADER_NAME;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.BEARER;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Security.CANNOT_SET_USER_AUTHENTICATION_ERROR_MESSAGE;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

    private JwtUtils jwtUtils;
    private BlockchainAccountingUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = parseJwt(request);

            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userNameFromJwtToken);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            }
        } catch (Exception e) {
            log.error(CANNOT_SET_USER_AUTHENTICATION_ERROR_MESSAGE + ": ", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String authHeader = Optional
                .ofNullable(request.getHeader(AUTHORIZATION_HEADER_NAME))
                .orElse(null);

        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(BEARER)) {
            return authHeader.substring(BEARER.length());
        }

        return null;
    }


    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setUserDetailsService(BlockchainAccountingUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
