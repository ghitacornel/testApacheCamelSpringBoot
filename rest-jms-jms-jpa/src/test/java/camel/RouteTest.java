package camel;

import camel.model.CustomMessage;
import camel.repository.MessageDbRepository;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@CamelSpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteTest {

    // Spring will inject the random port assigned to the web server
    @LocalServerPort
    int webServerPort;

    RestClient restClient = RestClient.create();

    @Autowired
    MessageDbRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testPostNoProcessor() {

        Assertions.assertThat(repository.findAll()).isEmpty();

        CustomMessage request = new CustomMessage(1);

        ResponseEntity<String> entity = restClient.post()
                .uri("http://localhost:" + webServerPort + "/xxx/jms")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request).retrieve().toEntity(String.class);

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(entity.getBody()).isNull();

        Assertions.assertThat(repository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(repository.findAll().getFirst().getId()).isEqualTo(1);
        Assertions.assertThat(repository.findAll().getFirst().getLogs()).isEqualTo("[RestJmsComponent processed, Queue1Queue2 processed, Queue2JPA processed]");
    }


}
