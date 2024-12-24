package backend.properties_crud.Accessing_endpoints_requiring_registration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationTest {

  @Autowired private MockMvc mockMvc;

  private final String encodedCredentials =
      getEncodedCredentials("storagedecentralized@gmail.com", "123456789");

  /* Inside database before using password encoder we needed to store password prefixing "noop"*/

  @Test
  @Order(1)
  public void testRegistration() throws Exception {
    String url = "/registration";
    String requestBody =
        """
        {
            "firstName":"storage",
            "lastName":"decentralized",
            "email":"storagedecentralized@gmail.com",
            "password":"123456789",
            "phoneNumber":"1234567890"
        }
        """;
    String expectedResponseBody =
        """
        {
            "firstName":"storage",
            "lastName":"decentralized",
            "email":"storagedecentralized@gmail.com",
            "phoneNumber":"1234567890"
        }
        """;
    mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(content().json(expectedResponseBody));
  }

  @Test
  @Order(2)
  public void testSuccessfulLogin() throws Exception {

    mockMvc
        .perform(get("/").header("Authorization", "Basic " + encodedCredentials))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(Matchers.equalTo("Greetings!! Glad to see you here")))
        .andExpect(cookie().doesNotExist("JSESSIONID"));

  }

  private String getEncodedCredentials(String username, String password) {
    String credentials = username + ":" + password;
    return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
  }
}
