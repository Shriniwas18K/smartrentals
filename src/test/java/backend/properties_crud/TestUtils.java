package backend.properties_crud;

public class TestUtils {
    
  public static String getEncodedCredentials(String username, String password) {
    String credentials = username + ":" + password;
    return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
  }
}
