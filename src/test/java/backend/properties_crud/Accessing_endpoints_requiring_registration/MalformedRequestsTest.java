package backend.properties_crud.Accessing_endpoints_requiring_registration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MalformedRequestsTest {

  @Autowired private MockMvc mockMvc;
  
  @Test
  public void testFailedLogin() throws Exception {
    mockMvc
        .perform(get("/").header("Authorization", "Basic " + "foo-bar"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testMissingAuthorizationHeader() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isForbidden());
  }

}
