package net.giteye;

import com.thebeastshop.forest.springboot.annotation.ForestScan;
import net.giteye.property.GeProperty;
import net.giteye.property.QCloudProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
@EnableConfigurationProperties({GeProperty.class, QCloudProperty.class})
@MapperScan("net.giteye.db.mapper")
@ForestScan({"net.giteye.client.gitee","net.giteye.client.github","net.giteye.client.wxmp"})
public class Runner {

	@Bean
	public GracefulShutdown gracefulShutdown() {
		return new GracefulShutdown();
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
		tomcatServletWebServerFactory.addConnectorCustomizers(gracefulShutdown());
		return tomcatServletWebServerFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(Runner.class, args);
	}

}
