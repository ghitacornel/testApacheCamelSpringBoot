package camel;

import camel.route.model.PersonRequest;
import camel.route.model.PersonResponse;
import camel.route.repository.PersonRepository;
import camel.route.repository.entity.Person;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@CamelSpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestJdbcJpaRouteTest {

    // Spring will inject the random port assigned to the web server
    @LocalServerPort
    int webServerPort;

    RestClient restClient = RestClient.create();

    @Autowired
    PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
        personRepository.save(Person.builder().id(1).name("first").age(111).build());
    }

    @Test
    public void testGet() {
        ResponseEntity<String> response = restClient.get()
                .uri("http://localhost:" + webServerPort + "/camel/api/1")
                .retrieve().toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"id\":1,\"name\":\"first\",\"age\":111}");
    }

    @Test
    public void testGetNotFound() {
        assertThatThrownBy(() -> restClient.get()
                .uri("http://localhost:" + webServerPort + "/camel/api/-1")
                .retrieve().toEntity(String.class)).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    public void testPostNoProcessor() {
        PersonRequest personRequest = new PersonRequest("none", 2, "ion", 11);

        PersonResponse response = restClient.post()
                .uri("http://localhost:" + webServerPort + "/camel/api")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personRequest).retrieve().body(PersonResponse.class);

        assertThat(response).isEqualTo(PersonResponse.builder().name("ion").age(11).build());
    }

    @Test
    public void testPostJPAProcessor() {
        PersonRequest personRequest = new PersonRequest("jpa", 3, "gheorge", 12);

        PersonResponse response = restClient.post()
                .uri("http://localhost:" + webServerPort + "/camel/api")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personRequest).retrieve().body(PersonResponse.class);
        assertThat(response).isEqualTo(PersonResponse.builder().id(3).name("gheorge").age(12).build());
    }

    @Test
    public void testPostJPAProcessorBadData() {
        PersonRequest personRequest = new PersonRequest("jpa", 33, "   ", 5);

        assertThatThrownBy(() -> restClient.post()
                .uri("http://localhost:" + webServerPort + "/camel/api")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personRequest).retrieve().toEntity(String.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 Bad Request: \"\"Could not commit JPA transaction\"\"");
    }

    @Test
    public void testPostJDBCProcessor() {
        PersonRequest personRequest = new PersonRequest("jdbc", 4, "vasile", 13);

        PersonResponse response = restClient.post()
                .uri("http://localhost:" + webServerPort + "/camel/api")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personRequest).retrieve().body(PersonResponse.class);
        assertThat(response).isEqualTo(PersonResponse.builder().id(4).name("vasile").age(13).build());
    }

}
