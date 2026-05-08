package org.example;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.example.model.RequestDTO;
import org.example.model.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@CamelSpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestRestTest {

    // Spring will inject the random port assigned to the web server
    @LocalServerPort
    int webServerPort;

    RestClient restClient;

    @BeforeEach
    void beforeAll() {
        restClient = RestClient.create("http://localhost:" + webServerPort + "/camel");
    }

    @Test
    public void testGet() {
        {
            ResponseEntity<String> response = restClient.get()
                    .uri("/person/hello")
                    .retrieve().toEntity(String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("Hello World");
        }
        {
            ResponseEntity<String> response = restClient.get()
                    .uri("/person/bye")
                    .retrieve().toEntity(String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("<html><body>Bye World</body></html>");
        }
        {
            ResponseEntity<String> response = restClient.get()
                    .uri("/person/congrats/John")
                    .retrieve().toEntity(String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("Congrats John");
        }
        {
            ResponseEntity<String> response = restClient.get()
                    .uri("/person/congrats/John?title=Sir")
                    .retrieve().toEntity(String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("Congrats Sir John");
        }
    }

    @Test
    public void testPost() {

        RequestDTO request = RequestDTO.builder().name("John").build();
        ResponseDTO response = ResponseDTO.builder().id(1).name("John").build();

        ResponseEntity<ResponseDTO> responseEntity = restClient.post()
                .uri("/person")
                .body(request)
                .retrieve().toEntity(ResponseDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(response);

    }

    @Test
    public void testDelete() {

        ResponseEntity<Void> response = restClient.delete()
                .uri("/person/123")
                .retrieve().toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();

    }

}
