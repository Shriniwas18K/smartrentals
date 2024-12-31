package backend.properties_crud;

import static backend.properties_crud.TestUtils.getEncodedCredentials;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserReadAndUpdationAndRemovalTest {

  @Autowired private MockMvc mockMvc;

  /* Initially this file contained four tests expected to execute
   * sequentailly using order annotation,it was observed that put
   * request of updation was being executed prior to post of
   * registration by ide test runner. Internally it is parrallel
   * test execution so we grouped logic into single test sequentially
   *
   * we discovered that above stuff was happening by setting
   * log level of spring security to DEBUG in logback.xml
   * and checking the log file, remaining root log level was ERROR
   * which filtered the log file itself to show logs only of spring security.
   */

  @Test
  void user_read_and_updation_and_removal_test() throws Exception {
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
    mockMvc.perform(
        post("/registration").contentType(MediaType.APPLICATION_JSON).content(requestBody));

    mockMvc
        .perform(
            get("/user/profile")
                .header(
                    "Authorization",
                    "Basic "
                        + getEncodedCredentials("storagedecentralized@gmail.com", "123456789")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User retrieved."))
        .andExpect(jsonPath("$.data.firstName").value("storage"))
        .andExpect(jsonPath("$.data.lastName").value("decentralized"))
        .andExpect(jsonPath("$.data.phone").value("1234567890"))
        .andExpect(jsonPath("$.data.email").value("storagedecentralized@gmail.com"));

    requestBody =
        """
            {
                "firstName": "storage",
                "lastName": "decent",
                "email": "storagedecentralized@gmail.com",
                "password": "9765735435",
                "phone": "1234567890"
            }
        """;
    mockMvc
        .perform(
            put("/user/update")
                .header(
                    "Authorization",
                    "Basic " + getEncodedCredentials("storagedecentralized@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.message").value("User details updated."))
        .andExpect(jsonPath("$.data.firstName").value("storage"))
        .andExpect(
            jsonPath("$.NOTE")
                .value(
                    "As password is updated hence now onwards each request should contain updated"
                        + " password in the authorization header."));

    /* after updation of password we need to put updated password in header */
    mockMvc
        .perform(
            get("/user/profile")
                .header(
                    "Authorization",
                    "Basic "
                        + getEncodedCredentials("storagedecentralized@gmail.com", "9765735435")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User retrieved."))
        .andExpect(jsonPath("$.data.firstName").value("storage"))
        .andExpect(jsonPath("$.data.lastName").value("decent"))
        .andExpect(jsonPath("$.data.phone").value("1234567890"))
        .andExpect(jsonPath("$.data.email").value("storagedecentralized@gmail.com"));

    mockMvc
        .perform(
            delete("/user/delete")
                .header(
                    "Authorization",
                    "Basic "
                        + getEncodedCredentials("storagedecentralized@gmail.com", "9765735435")))
        .andExpect(status().isAccepted())
        .andExpect(
            jsonPath("$.message").value("User and associated properties removed."));

    mockMvc
        .perform(
            get("/")
                .header(
                    "Authorization",
                    "Basic "
                        + getEncodedCredentials("storagedecentralized@gmail.com", "9765735435")))
        .andExpect(status().isUnauthorized());
  }
}
