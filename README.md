# spring-security-all

### Insightful and useful links:

- Step By Step guide with details insight , take a look on this [youtube video](https://www.youtube.com/watch?v=vQ2B3qAWuAU&list=PLLhgRnf2WBVQe1iPUuNZnMmlqK6vd_o59&index=2)
- Spring Security Architecture [link](https://docs.spring.io/spring-security/reference/servlet/architecture.html)


### Steps By Step Guide
- Create a project using [Spring Initializr](https://start.spring.io/)
- add the expected dependency
  - Spring Web
  - lombok
  - thyemeleaf
- Create a controller
```java
@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "index";
    }
}
```
- Write a simple html page in resources/templates/index.html
```html
<html lang="en">
<head>
    <title>Welcome</title>
</head>
<body>
 <div>
     <h1>Welcome</h1>
 </div>
</body>
</html>
```

- Write a test for the controller
```java
@WebMvcTest
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldDisplayHomePage() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}
```
- Run the application
- Access it with http://localhost:8080/
- Let's secure the application
- go to spring initializr and add the dependency for spring security
- add the follwing dependency
  - Spring Security
  - If you observe the pom.xml file, you will see that `spring security` is added as a dependency along with `thymeleaf-extras-springsecurity6` and `spring-security-test`
- click on the explorer button in the initializr and copy the pom.xml content and replace it with the existing pom.xml
- Build the application and run it.
- Access the application with http://localhost:8080/.
- you will see a login page - don't worry , that's the default behavior of spring security.
- you can login with the username `user` and the password will be generated in the console.
  - Let's try to understand what's going on here.
    - Let's try to enable TRACE for `org..
    - add the below property in the `application.properties` file
    ```properties
    ```
    - Now, let's try to login again and see what's going on in the console.
    - you will see a lot of logs in the console.
    ```shell
    2023-04-28T16:06:40.448+02:00 DEBUG 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing GET /login
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking DisableEncodeUrlFilter (1/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking WebAsyncManagerIntegrationFilter (2/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking SecurityContextHolderFilter (3/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking HeaderWriterFilter (4/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking CsrfFilter (5/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.csrf.CsrfFilter         : Did not protect against CSRF since request did not match CsrfNotRequired [TRACE, HEAD, GET, OPTIONS]
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking LogoutFilter (6/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.s.w.a.logout.LogoutFilter            : Did not match request to Ant [pattern='/logout', POST]
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking UsernamePasswordAuthenticationFilter (7/15)
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] w.a.UsernamePasswordAuthenticationFilter : Did not match request to Ant [pattern='/login', POST]
    2023-04-28T16:06:40.448+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Invoking DefaultLoginPageGeneratingFilter (8/15)
    2023-04-28T16:06:40.450+02:00 TRACE 2219 --- [nio-8080-exec-2] o.s.s.w.header.writers.HstsHeaderWriter  : Not injecting HSTS header since it did not match request to [Is Secure]

    ```
    - Let's try to understand what's going on here.
    - try to observer the log and see which are the class being invoked and what's the order of invocation.
    - at the end of the log, you will see that `DefaultLoginPageGeneratingFilter` is invoked. Open that class and try to understand what that class is doing
    - Spring security is a filter based framework.
    - It has a chain of filters and each filter is responsible for a specific task.
    - The first filter in the chain is `UsernamePasswordAuthenticationFilter` which is responsible for authenticating the user.
    - More details on the architecture of spring security can be found [here](https://docs.spring.io/spring-security/reference/servlet/architecture.html)
- Let's try override the default behavior of spring security. and some inmemory user details.
- Create a class `SecurityConfiguration`
```java
@Configuration
public class SecurityConfiguration {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService inmemoryUsers() {
        InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
        var bob = new User("bob", passwordEncoder().encode("1234"), Collections.emptyList());
        var bil = User.builder()
                .username("bil")
                .password(passwordEncoder().encode("321"))
                //roles - are like ADMIN, USER, STUDENT, TEACHER
                .roles("USER")
                //authorities - are like read, write, delete, update
                .authorities("read")
                .build();
        users.createUser(bob);
        users.createUser(bil);

        return users;
    }
}
```
- now we have 2 user called `bob` and `bil` and both of them have different password.
- `bob` has no roles and `bil` has a role called `USER` and an authority called `read`
- Lets now login with `bob` and `bil` and see what happens. I'm sure you will be able to login and see the expected result.
- we can follow this architecture [diagram](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html) , add the debug point and start the flow than analyze.
- `thymeleaf-extras-springsecurity6` dependency will give extra features to thymeleaf to work with spring security.[explore](https://www.thymeleaf.org/doc/articles/springsecurity.html) for more details.
- with this we have extended our `login.html` with this. with this we will have access to `authentication` object to get the user details to be used on the needed page.
```html
<!doctype html>
<html lang="en" xmlns:sec="http://www.w3.org/1999/xhtml">
<head>
    <title>Welcome</title>
</head>
<body>
 <div>
     <h1>Welcome</h1>
     <div sec:authorize="isAuthenticated()">
         <span sec:authentication="principal"></span>
     </div>
 </div>
</body>
</html>
```
- Our Controller might be failing by now because of spring security. Let's fix that.
- TO fix modify your test like this. The additional details we have added on the test was `@WithMockUser` annotation. This annotation will create a mock user with the given details and will be used for testing.
```java
@WebMvcTest
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void authenticatedUserShouldDisplayHomePage() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}
```

### Oauth2 support
- Watch [this video](https://www.youtube.com/watch?v=40BxatEr5aE&list=PLLhgRnf2WBVQe1iPUuNZnMmlqK6vd_o59&index=3) for more details.