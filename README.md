# Spring security

Starting with Spring Security 5.7.0-M2 the WebSecurityConfigurerAdapter has been deprecated, and the Spring Security 
team encourages users to move towards a component-based security configuration.

The initial implementations in this project were done using an older version fo Spring Security, so I created 2 Spring profiles `securityCfgOld` and `securityCfgNew` and
corresponding configuration classes (e.g. one using the WebSecurityConfigurerAdapter, others based on the new concept)

Additional info on how to make the transition
[https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)


**************************************************

## Configuring HttpSecurity
In Spring Security 5.4 we introduced the ability to configure HttpSecurity by creating a SecurityFilterChain bean.

Below is an example configuration using the WebSecurityConfigurerAdapter that secures all endpoints with HTTP Basic:

    @Configuration
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests((authz) -> authz
                    .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        }
    
    }

Going forward, the recommended way of doing this is registering a SecurityFilterChain bean:

    @Configuration
    public class SecurityConfiguration {
    
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests((authz) -> authz
                    .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
            return http.build();
        }
    
    }

**************************************************

## Configuring WebSecurity
In Spring Security 5.4 we also introduced the WebSecurityCustomizer.

The WebSecurityCustomizer is a callback interface that can be used to customize WebSecurity.

Below is an example configuration using the WebSecurityConfigurerAdapter that ignores requests that match /ignore1 or /ignore2:

    @Configuration
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/ignore1", "/ignore2");
        }
    
    }

Going forward, the recommended way of doing this is registering a WebSecurityCustomizer bean:

    @Configuration
    public class SecurityConfiguration {
    
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
        }
    
    }

WARNING: If you are configuring WebSecurity to ignore requests, consider using permitAll via HttpSecurity#authorizeHttpRequests instead. See the configure Javadoc for additional details.

**************************************************

## LDAP Authentication
In Spring Security 5.7 we introduced the EmbeddedLdapServerContextSourceFactoryBean, LdapBindAuthenticationManagerFactory and LdapPasswordComparisonAuthenticationManagerFactory which can be used to create an embedded LDAP Server and an AuthenticationManager that performs LDAP authentication.

Below is an example configuration using WebSecurityConfigurerAdapter the that creates an embedded LDAP server and an AuthenticationManager that performs LDAP authentication using bind authentication:

    @Configuration
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .ldapAuthentication()
                .userDetailsContextMapper(new PersonContextMapper())
                .userDnPatterns("uid={0},ou=people")
                .contextSource()
                .port(0);
        }
    
    }

Going forward, the recommended way of doing this is using the new LDAP classes:

    @Configuration
    public class SecurityConfiguration {
    
        @Bean
        public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
            EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
            EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
            contextSourceFactoryBean.setPort(0);
            return contextSourceFactoryBean;
        }
    
        @Bean
        AuthenticationManager ldapAuthenticationManager(BaseLdapPathContextSource contextSource) {
            LdapBindAuthenticationManagerFactory factory = 
                new LdapBindAuthenticationManagerFactory(contextSource);
            factory.setUserDnPatterns("uid={0},ou=people");
            factory.setUserDetailsContextMapper(new PersonContextMapper());
            return factory.createAuthenticationManager();
        }
    }

**************************************************

## JDBC Authentication
Below is an example configuration using the WebSecurityConfigurerAdapter with an embedded DataSource that is initialized with the default schema and has a single user:

    @Configuration
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
        }
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
            auth.jdbcAuthentication()
                .withDefaultSchema()
                .dataSource(dataSource())
                .withUser(user);
        }
    }

The recommended way of doing this is registering a JdbcUserDetailsManager bean:

    @Configuration
    public class SecurityConfiguration {
    
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build();
        }
    
        @Bean
        public UserDetailsManager users(DataSource dataSource) {
            UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
            JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
            users.createUser(user);
            return users;
        }
    }

Note: In these examples, we use the method User.withDefaultPasswordEncoder() for readability. It is not intended for production and instead we recommend hashing your passwords externally. One way to do that is to use the Spring Boot CLI as described in the reference documentation.

**************************************************

## In-Memory Authentication
Below is an example configuration using the WebSecurityConfigurerAdapter that configures an in-memory user store with a single user:

    @Configuration
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
            auth.inMemoryAuthentication()
            .withUser(user);
        }
    }

The recommended way of doing this is registering an InMemoryUserDetailsManager bean:

    @Configuration
    public class SecurityConfiguration {

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
            return new InMemoryUserDetailsManager(user);
        }
    }

Note: In these examples, we use the method User.withDefaultPasswordEncoder() for readability. It is not intended for production and instead we recommend hashing your passwords externally. One way to do that is to use the Spring Boot CLI as described in the reference documentation.

**************************************************

## Global AuthenticationManager
To create an AuthenticationManager that is available to the entire application you can simply register the AuthenticationManager as a @Bean.

This type of configuration is shown above in the LDAP Authentication example.

**************************************************

## Local AuthenticationManager
In Spring Security 5.6 we introduced the method HttpSecurity#authenticationManager that overrides the default AuthenticationManager for a specific SecurityFilterChain.
Below is an example configuration that sets a custom AuthenticationManager as the default:

    @Configuration
    public class SecurityConfiguration {
    
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests((authz) -> authz
                    .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .authenticationManager(new CustomAuthenticationManager());
            return http.build();
        }
    
    }

## Accessing the local AuthenticationManager
The local AuthenticationManager can be accessed in a custom DSL. This is actually how Spring Security internally implements methods like HttpSecurity.authorizeRequests().

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new CustomFilter(authenticationManager));
        }
    
        public static MyCustomDsl customDsl() {
            return new MyCustomDsl();
        }
    }

The custom DSL can then be applied when building the SecurityFilterChain:

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ...
        http.apply(customDsl());
        return http.build();
    }