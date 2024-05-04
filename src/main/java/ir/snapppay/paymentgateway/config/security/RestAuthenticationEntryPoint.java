//package ir.snapppay.paymentgateway.config.security;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
////@Primary
//@RequiredArgsConstructor
//@Component("restAuthenticationEntryPoint")
//public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {
//
//    private final HandlerExceptionResolver resolver;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
//        resolver.resolveException(request, response, null, exception);
//    }
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
//        resolver.resolveException(request, response, null, exception);
//    }
//}
