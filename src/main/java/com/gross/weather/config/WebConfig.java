    package com.gross.weather.config;

    import com.gross.weather.interceptor.AuthInterceptor;
    import com.gross.weather.service.SessionService;
    import com.gross.weather.service.UserService;
    import liquibase.integration.spring.SpringLiquibase;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.PropertySource;
    import org.springframework.core.env.Environment;
    import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
    import org.springframework.jdbc.datasource.DriverManagerDataSource;
    import org.springframework.orm.jpa.JpaTransactionManager;
    import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
    import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.transaction.PlatformTransactionManager;
    import org.springframework.transaction.annotation.EnableTransactionManagement;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
    import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    import org.thymeleaf.spring6.SpringTemplateEngine;
    import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
    import org.thymeleaf.spring6.view.ThymeleafViewResolver;

    import javax.sql.DataSource;
    import java.util.Objects;
    import java.util.Properties;

    @Configuration
    @ComponentScan("com.gross.weather")
    @PropertySource("classpath:hibernate.properties")
    @EnableTransactionManagement
    @EnableJpaRepositories("com.gross.weather.repositories")
    @EnableWebMvc

    public class WebConfig implements WebMvcConfigurer {
        private final ApplicationContext applicationContext;
        private final Environment environment;


        @Autowired
        public WebConfig(ApplicationContext applicationContext, Environment environment
                         ) {
            this.applicationContext = applicationContext;
            this.environment = environment;

        }

        @Bean
        public AuthInterceptor authInterceptor(SessionService sessionService, UserService userService) {
            return new AuthInterceptor(sessionService, userService);
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authInterceptor(null, null)) // null будет заменен Spring при инъекции
                    .excludePathPatterns("/resources/**");
        }
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/**")
                    .addResourceLocations("/resources/");

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public SpringResourceTemplateResolver templateResolver() {
            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
            templateResolver.setApplicationContext(applicationContext);
            templateResolver.setPrefix("/WEB-INF/views/");
            templateResolver.setSuffix(".html");
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setCacheable(false);

            return templateResolver;
        }

        @Bean
        public SpringTemplateEngine templateEngine() {
            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.setTemplateResolver(templateResolver());
            templateEngine.setEnableSpringELCompiler(true);

            return templateEngine;
        }

        @Bean
        public ThymeleafViewResolver viewResolver() {
            ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
            viewResolver.setTemplateEngine(templateEngine());
            viewResolver.setCharacterEncoding("UTF-8");
            viewResolver.setOrder(1);

            return viewResolver;
        }


        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("hibernate.connection.driver_class")));
            dataSource.setUrl(environment.getProperty("hibernate.connection.url"));
            dataSource.setUsername(environment.getProperty("hibernate.connection.username"));
            dataSource.setPassword(environment.getProperty("hibernate.connection.password"));
            return dataSource;
        }

        private Properties hibernateProperties() {
            Properties properties = new Properties();
            properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
            properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));

            return properties;
        }

        @Bean
        public SpringLiquibase liquibase(DataSource dataSource) {

            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(dataSource);
            liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml"); // Путь к файлу миграций
            liquibase.setDropFirst(false);
            liquibase.setShouldRun(false); // Можно выключить, если не хочешь запускать Liquibase при старте
            return liquibase;
        }


        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                    new LocalContainerEntityManagerFactoryBean();
            entityManagerFactoryBean.setDataSource(dataSource());
            entityManagerFactoryBean.setPackagesToScan("com.gross.weather.model");

            final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
            entityManagerFactoryBean.setJpaProperties(hibernateProperties());

            return entityManagerFactoryBean;
        }

           /* @Bean
            public PlatformTransactionManager hibernateTransactionManager() {
                HibernateTransactionManager transactionManager = new HibernateTransactionManager();
                transactionManager.setSessionFactory(sessionFactory().getObject());

                return transactionManager;
            }*/

        @Bean
        public PlatformTransactionManager transactionManager() {
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

            return transactionManager;
        }
    }
