package com.bangshinchul.backend.auth;

import com.bangshinchul.backend.BackendApplicationTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("local")
public class AuthControllerTest extends BackendApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void loginTest() throws Exception{
        Auth request = new Auth();
        request.setUsername("admin");
        String password = passwordEncoder.encode("12345");
//        request.setPassword("$2a$10$BujuNOQk0YCaIZ/jQj.VgO3zUsNmNqIscAIDtOcDgreQmzu4xqaYS");
        request.setPassword(password);

        ObjectMapper om = new ObjectMapper();

        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", is(request.getUsername().toUpperCase())))
                .andExpect(jsonPath("$.authRoles[*].role", hasItem("USER"))) ;

    }
}