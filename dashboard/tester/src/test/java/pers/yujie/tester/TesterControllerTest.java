package pers.yujie.tester;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class TesterControllerTest {

  static final String URL = "https://template-8gf1js6t49b08525-1316531086.tcloudbaseapp.com/text.html";

  @Autowired
  MockMvc mockMvc;

  @Test
  void testChrome() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("url", Collections.singletonList(URL));
    params.put("mode", Collections.singletonList("chrome"));

    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/webdriver").params(params))
          .andExpect(status().isOk());
    });
  }

  @Test
  void testFirefox() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("url", Collections.singletonList(URL));
    params.put("mode", Collections.singletonList("firefox"));

    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/webdriver").params(params))
          .andExpect(status().isOk());
    });
  }

  @Test
  @SuppressWarnings("all")
  void testValidateURL() {
    String validURL = "https://www.google.com";
    String invalidURL = "ttps://www.google.com";

    assertTrue((boolean) ReflectionTestUtils.invokeMethod(
        new TesterController(), "validateURL", validURL));

    assertFalse((boolean) ReflectionTestUtils.invokeMethod(
        new TesterController(), "validateURL", invalidURL));
  }
}