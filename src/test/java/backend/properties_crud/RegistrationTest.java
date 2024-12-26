package backend.properties_crud;

import static backend.properties_crud.TestUtils.getEncodedCredentials;
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

  @Test
  @Order(0)
  void test_unauthorized_access() throws Exception {
    mockMvc
        .perform(get("/"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(1)
  void test_validation_of_request_body_in_registration() throws Exception {
    String url = "/registration";
    String invalidRequestBody =
        """
        {
            "firstName":"storage",
            "lastName":"decentralized",
            "email":"storagedecentralized@gmail.com",
            "password":"1234569",
            "phone":"123456780"
        }
        """;
    mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(invalidRequestBody))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @Order(2)
  void test_registration_and_accessibility() throws Exception {
    String url = "/registration";
    String requestBody =
        """
        {
            "firstName":"storage",
            "lastName":"decentralized",
            "email":"storagedecentralized@gmail.com",
            "password":"123456789",
            "phone":"1234567890"
        }
        """;
    String expectedResponseBody =
        """
        {
            "data": {
                "firstName": "storage",
                "lastName": "decentralized",
                "email": "storagedecentralized@gmail.com",
                "phone": "1234567890"
            },
            "message": "Registration Successful."
        }
        """;
    mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(content().json(expectedResponseBody));
  }

  @Test
  @Order(3)
  void testSuccessfulLogin() throws Exception {

    mockMvc
        .perform(get("/").header("Authorization", "Basic " + encodedCredentials))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value(Matchers.equalTo("Greetings!! Glad to see you here")))
        .andExpect(cookie().doesNotExist("JSESSIONID"));
  }
}
