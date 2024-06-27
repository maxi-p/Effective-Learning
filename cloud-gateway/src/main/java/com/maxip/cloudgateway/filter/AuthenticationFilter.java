package com.maxip.cloudgateway.filter;

import com.maxip.cloudgateway.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

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
            ServerHttpRequest request = null;
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
                    request = exchange
                            .getRequest()
                            .mutate()
                            .header("loggedId", jwtService.extractId(token).toString())
                            .build();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unauthorized access to application", e);
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config
    {

    }

}

