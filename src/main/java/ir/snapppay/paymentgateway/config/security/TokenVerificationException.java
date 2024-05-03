package ir.snapppay.paymentgateway.config.security;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@ExceptionMapping(statusCode = HttpStatus.UNAUTHORIZED, errorCode = "token.verification_failed")
public class TokenVerificationException extends AuthenticationException {

    public TokenVerificationException() {
        super("Token is not valid");
    }
}
