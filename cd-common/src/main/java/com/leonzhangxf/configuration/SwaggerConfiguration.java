package com.leonzhangxf.configuration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The swagger configuration.
 *
 * @author leonzhangxf 20180808
 */
@EnableSwagger2
@ConfigurationProperties("swagger")
public class SwaggerConfiguration {

	/**
	 * If enable swagger api doc.
	 */
	private boolean enabled;

	/**
	 * The custom host when use some proxy url to your service.
	 */
	private String host;

	private Principal contact;

	private String title;

	private String description;

	private String version;

	@Bean
	public Docket api() {
		Docket docket;
		ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).select();

		if (enabled) {
			docket = builder.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
					.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
					.build();
		} else {
			docket = builder.apis(RequestHandlerSelectors.none())
					.paths(PathSelectors.none())
					.build();
		}

		docket.apiInfo(apiInfo());

		if (StringUtils.hasText(host))
			docket.host(host);

		return docket;
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo;

		ApiInfoBuilder builder = new ApiInfoBuilder();

		if (StringUtils.hasText(title))
			builder.title(title);

		if (StringUtils.hasText(description))
			builder.description(description);

		if (null != contact && contact.fullValue())
			builder.contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()));

		if (StringUtils.hasText(version))
			builder.version(this.version);

		apiInfo = builder.build();
		return apiInfo;
	}

	class Principal {

		private String name;

		private String url;

		private String email;

		private boolean fullValue() {
			return StringUtils.hasText(name)
					&& StringUtils.hasText(url)
					&& StringUtils.hasText(email);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Principal getContact() {
		return contact;
	}

	public void setContact(Principal contact) {
		this.contact = contact;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
