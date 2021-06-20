package com.mastersessay.blockchain.accounting.util;

import com.mastersessay.blockchain.accounting.model.user.BlockchainAccountingUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Security.*;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${blockchainAccounting.app.jwtSecret}")
    private String jwtSecret;
    @Value("${blockchainAccounting.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        BlockchainAccountingUserDetails userPrincipal = (BlockchainAccountingUserDetails) authentication.getPrincipal();

        return Jwts
                .builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration((new Date((new Date()).getTime() + jwtExpirationMs)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

            return true;
        } catch (SignatureException e) {
            log.error(INVALID_JWT_SIGNATURE_ERROR_MESSAGE + ": {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(INVALID_JWT_TOKEN_ERROR_MESSAGE + ": {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(JWT_TOKEN_EXPIRED_ERROR_MESSAGE + ": {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(JWT_TOKEN_UNSUPPORTED_ERROR_MESSAGE + ": {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(JWT_CLAIMS_STRING_EMPTY_ERROR_MESSAGE + ": {}", e.getMessage());
        }

        return false;
    }
}
