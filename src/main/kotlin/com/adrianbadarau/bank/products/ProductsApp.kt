package com.adrianbadarau.bank.products

import com.adrianbadarau.bank.products.config.ApplicationProperties
import io.github.jhipster.config.DefaultProfileUtil
import io.github.jhipster.config.JHipsterConstants
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.net.InetAddress
import java.net.UnknownHostException
import javax.annotation.PostConstruct


@SpringBootApplication
@EnableConfigurationProperties(LiquibaseProperties::class, ApplicationProperties::class)
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
class ProductsApp(private val env: Environment) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Initializes products.
     *
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     *
     * You can find more information on how profiles work with JHipster on [https://www.jhipster.tech/profiles/]("https://www.jhipster.tech/profiles/").
     */
    @PostConstruct
    fun initApplication() {
        val activeProfiles = env.activeProfiles
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time."
            )
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " +
                    "run with both the 'dev' and 'cloud' profiles at the same time."
            )
        }
    }

    @Bean("threadPoolTaskExecutor")
    fun getAsyncExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 2
        executor.maxPoolSize = 10
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.threadNamePrefix = "Async-"
        return executor
    }

    companion object {
        /**
         * Main method, used to run the application.
         *
         * @param args the command line arguments.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val env = runApplication<ProductsApp>(*args) { DefaultProfileUtil.addDefaultProfile(this) }.environment
            logApplicationStartup(env)
        }

        @JvmStatic
        private fun logApplicationStartup(env: Environment) {
            val log = LoggerFactory.getLogger(ProductsApp::class.java)

            var protocol = "http"
            if (env.getProperty("server.ssl.key-store") != null) {
                protocol = "https"
            }
            val serverPort = env.getProperty("server.port")
            var contextPath = env.getProperty("server.servlet.context-path")
            if (contextPath.isNullOrBlank()) {
                contextPath = "/"
            }
            var hostAddress = "localhost"
            try {
                hostAddress = InetAddress.getLocalHost().hostAddress
            } catch (e: UnknownHostException) {
                log.warn("The host name could not be determined, using `localhost` as fallback")
            }
            log.info(
                "\n----------------------------------------------------------\n\t" +
                    "Application '{}' is running! Access URLs:\n\t" +
                    "Local: \t\t{}://localhost:{}{}\n\t" +
                    "External: \t{}://{}:{}{}\n\t" +
                    "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.activeProfiles
            )

            var configServerStatus = env.getProperty("configserver.status")
            if (configServerStatus == null) {
                configServerStatus = "Not found or not setup for this application"
            }
            log.info(
                "\n----------------------------------------------------------\n\t" +
                    "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus
            )
        }
    }
}
