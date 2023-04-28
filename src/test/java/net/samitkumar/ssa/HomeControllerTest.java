package net.samitkumar.ssa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
