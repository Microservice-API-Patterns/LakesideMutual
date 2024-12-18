package com.lakesidemutual.customerselfservice.tests.interfaces;

import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLoginEntity;
import com.lakesidemutual.customerselfservice.infrastructure.CustomerCoreRemoteProxy;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.configuration.JwtUtils;
import com.lakesidemutual.customerselfservice.interfaces.configuration.UnauthorizedHandler;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.SignupRequestDto;
import com.lakesidemutual.customerselfservice.tests.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTests {
    private String email;
    private String cleartextPassword;
    private UserLoginEntity userLogin;

    // This makes sure that the rate limiter is not active during unit-testing.
    @TestConfiguration
    static class AuthenticationControllerTestContextConfiguration {
        @Bean
        public HandlerInterceptor rateLimitInterceptor() {
            return new HandlerInterceptor() {
            };
        }

        @Bean
        public UnauthorizedHandler unauthorizedHandler() {
            return new UnauthorizedHandler();
        }

        ;
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils tokenUtils;

    @MockBean
    private CustomerCoreRemoteProxy customerCoreRemoteProxy;

    @MockBean
    private UserLoginRepository userRepository;

    @BeforeEach
    public void setUp() {
        email = "max@example.com";
        cleartextPassword = "123456";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(cleartextPassword);
        userLogin = new UserLoginEntity(email, hashedPassword, "ADMIN", null);
    }

    @Test
    public void whenUserAlreadyExists_thenSignUpFails() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto(email, cleartextPassword);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(userLogin);
        mvc.perform(post("/auth/signup").content(TestUtils.asJsonString(signupRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenUserDoesntExist_thenSignupSucceeds() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto(email, cleartextPassword);
        mvc.perform(post("/auth/signup").content(TestUtils.asJsonString(signupRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)));

        Mockito.verify(userRepository).save(Mockito.argThat(userLogin -> {
            if (!userLogin.getEmail().equals(email)) {
                return false;
            }

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches(cleartextPassword, userLogin.getPassword());
        }));
    }
}
