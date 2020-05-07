package springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스캔대상에서 분리 
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
 