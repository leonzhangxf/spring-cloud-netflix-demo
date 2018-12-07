package com.leonzhangxf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.*;

@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // access token 有效期，默认1小时
    private static final Integer DEFAULT_TOKEN_VALIDITY_SECONDS = 60 * 60;

    // refresh token 有效期，默认10分钟
    private static final Integer DEFAULT_REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 10;

    private RedisConnectionFactory redisConnectionFactory;

    private DataSource dataSource;

    /**
     * 用户信息service
     */
    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    /**
     * TODO
     * 这里测试，密码不进行加密，生产使用需要进行加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 使用redis存储token
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * token service
     */
    @Primary
    @Bean
    public DefaultTokenServices redisTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(DEFAULT_TOKEN_VALIDITY_SECONDS);
        tokenServices.setRefreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECONDS);
        return tokenServices;
    }

    /**
     * client 信息获取service
     * <p>
     * "authorization_code", "refresh_token", "client_credentials", "implicit", "password"
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        //后期再使用数据库来存储client数据源
//        return new JdbcClientDetailsService(dataSource);

        InMemoryClientDetailsService inMemoryClientDetailsService = new InMemoryClientDetailsService();
        Map<String, ClientDetails> clientDetailsStore = new HashMap<>();

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId("gateway");
        baseClientDetails.setClientSecret("gateway");
        baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "refresh_token",
                "client_credentials", "implicit", "password"));
        baseClientDetails.setScope(Arrays.asList("gateway"));
        Set<String> redirectUris = new HashSet<>();
        redirectUris.add("http://localhost:10002/");
        redirectUris.add("http://localhost:10002/login");
        baseClientDetails.setRegisteredRedirectUri(redirectUris);

        clientDetailsStore.put(baseClientDetails.getClientId(), baseClientDetails);
        inMemoryClientDetailsService.setClientDetailsStore(clientDetailsStore);
        return inMemoryClientDetailsService;
    }

    /**
     * oauth2 相关endpoint的安全配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // token key获取接口权限 /oauth/token_key
        security.tokenKeyAccess("permitAll()");
        // access_token验证接口权限 /oauth/check_token
        security.checkTokenAccess("permitAll()");

        //允许通过form表单来进行client认证
        security.allowFormAuthenticationForClients();

        //密码编码器
        security.passwordEncoder(passwordEncoder());
    }

    /**
     * 配置client列表
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * oauth2协议中相关endpoint的配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置 /oauth/token 允许请求的HTTP方法
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        //认证管理器
        endpoints.authenticationManager(authenticationManager);

        //用户信息服务
        endpoints.userDetailsService(userDetailsService);

        //token 服务
        endpoints.tokenServices(redisTokenServices());
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
