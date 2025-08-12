package br.com.fiap.festo.festo_webapp_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Configuração para agendamento de tarefas (manutenção preventiva)
 * 
 * @author Seu Nome
 * @version 1.0
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // TODO: Implementar configurações do scheduler
        // - Definir pool size
        // - Configurar thread naming
        // - Definir políticas de execução

        return scheduler;
    }
}
