package com.maxip.cloudgateway.filter;

import com.maxip.cloudgateway.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>
{
    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtService jwtService;

    public AuthenticationFilter()
    {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config)
    {
        return ((exchange, chain) ->
        {
            if (validator.isSecured.test(exchange.getRequest()))
            {
                if (!exchange.getRequest().getHeaders().containsKey("Authorization"))
                {
                    throw new RuntimeException("Authentication (header) is missing");
                }
                String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                String token = "";
                if (authHeader != null && authHeader.startsWith("Bearer "))
                {
                    token = authHeader.substring(7);
                }

                try
                {
                    jwtService.validateToken(token);
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unauthorized access to application", e);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config
    {

    }

}

