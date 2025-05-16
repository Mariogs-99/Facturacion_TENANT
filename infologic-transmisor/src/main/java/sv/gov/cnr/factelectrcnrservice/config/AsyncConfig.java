package sv.gov.cnr.factelectrcnrservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private final Integer coreCount = Runtime.getRuntime().availableProcessors();

    // Operación Normal Task
    @Bean(name = "dteNormalTaskExecutor")
    public Executor dteTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreCount);
        executor.setMaxPoolSize(coreCount * 2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("dteNormalTaskExecutor-");
        executor.initialize();
        return executor;
    }

    // Operación Contingencia Task
    @Bean(name = "dteContingenciaTaskExecutor")
    public Executor recepcionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Math.max(1, coreCount / 4));
        executor.setMaxPoolSize(Math.max(2, coreCount / 2));
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("dteContingenciaTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
