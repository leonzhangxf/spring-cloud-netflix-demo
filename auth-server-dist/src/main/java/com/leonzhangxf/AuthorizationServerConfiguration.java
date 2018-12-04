package com.leonzhangxf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // access token 有效期，默认12小时
    private static final Integer DEFAULT_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;

    // refresh token 有效期，默认30天
    private static final Integer DEFAULT_REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

    private RedisConnectionFactory redisConnectionFactory;

    private DataSource dataSource;

    /**
     * 用户信息service
     */
    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    /**
     * 使用redis存储token
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * client 信息
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        //后期再使用数据库来存储client数据源
//        return new JdbcClientDetailsService(dataSource);

        InMemoryClientDetailsService inMemoryClientDetailsService = new InMemoryClientDetailsService();
        Map<String, ClientDetails> clientDetailsStore = new HashMap<>();

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId("auth");
        baseClientDetails.setClientSecret("auth");
        baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "password", "refresh_token"));

        clientDetailsStore.put(baseClientDetails.getClientId(), baseClientDetails);
        inMemoryClientDetailsService.setClientDetailsStore(clientDetailsStore);
        return inMemoryClientDetailsService;
    }

    /**
     * token 处理
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(DEFAULT_TOKEN_VALIDITY_SECONDS);
        tokenServices.setRefreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECONDS);

        tokenServices.setClientDetailsService(clientDetailsService());
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // token key 的获取权限为所有均可
        security.tokenKeyAccess("permitAll()");
        // 检查 token 的权限为认证过的
        security.checkTokenAccess("isAuthenticated()");

        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        endpoints.setClientDetailsService(clientDetailsService());
        endpoints.tokenServices(defaultTokenServices());
    }

    @Autowired
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    @Qualifier("authUserDetailsService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
