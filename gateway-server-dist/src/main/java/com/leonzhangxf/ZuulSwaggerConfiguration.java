package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据zuul网关代理拆分swagger接口文档
 *
 * @author leonzhangxf 20181129
 */
@ConfigurationProperties("zuul")
public class ZuulSwaggerConfiguration extends SwaggerConfiguration {

    private static final String DEFAULT_SWAGGER_DOC_URL_SUFFIX = "/v2/api-docs";

    private String prefix;

    private Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();

    @Component
    @Primary
    class DocumentationConfig implements SwaggerResourcesProvider {

        @Override
        public List<SwaggerResource> get() {
            List<SwaggerResource> resources = new ArrayList<>();
            resolveResourcesByRoutes(resources);
            return resources;
        }

        private void resolveResourcesByRoutes(List<SwaggerResource> resources) {
            if (!CollectionUtils.isEmpty(routes)) {
                String serviceName;
                StringBuilder url;
                ZuulProperties.ZuulRoute zuulRoute;
                String serviceUrl;
                for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routes.entrySet()) {
                    serviceName = entry.getKey();
                    zuulRoute = entry.getValue();
                    url = new StringBuilder();
                    if (!StringUtils.hasText(serviceName) || null == zuulRoute
                            || !StringUtils.hasText(zuulRoute.getPath())
                            // 通过物理URL或者微服务内进行调用，必须至少有一个。
                            || (!StringUtils.hasText(zuulRoute.getUrl())
                            && !StringUtils.hasText(zuulRoute.getServiceId()))) {
                        continue;
                    }
                    serviceUrl = StringUtils.replace(zuulRoute.getPath(), "/**", "");
                    url.append(prefix).append(serviceUrl).append(DEFAULT_SWAGGER_DOC_URL_SUFFIX);

                    resources.add(swaggerResource(serviceName, url.toString()));
                }
            }
        }

        private SwaggerResource swaggerResource(String name, String url) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setUrl(url);
            return swaggerResource;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Map<String, ZuulProperties.ZuulRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, ZuulProperties.ZuulRoute> routes) {
        this.routes = routes;
    }
}
