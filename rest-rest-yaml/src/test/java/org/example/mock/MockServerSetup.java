package org.example.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.model.RequestDTO;
import org.example.model.ResponseDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

@Slf4j
//@WireMockTest
// doesn't work, need to investigate, till then just manual setup
public abstract class MockServerSetup {

    static final WireMockServer wireMockServer = new WireMockServer(8080, 8181);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void startWireMockServer() {
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMockServer() {
        wireMockServer.stop();
    }

    @BeforeAll
    @SneakyThrows
    static void setupExternalApplicationAsMock() {

        {
            stubFor(get("/external/simpleText")
                    .willReturn(ok("Hello World " + LocalDateTime.now())));
        }
        {
            stubFor(get("/external/simpleHtml")
                    .willReturn(ok("<html><body>Bye World</body></html>")));
        }
        {
            stubFor(get("/external/simpleParameters/John")
                    .willReturn(ok("Congrats John")));
            stubFor(get("/external/simpleParameters/John?title=Sir")
                    .willReturn(ok("Congrats Sir John")));
        }
        {
            RequestDTO request = RequestDTO.builder().name("John").build();
            ResponseDTO response = ResponseDTO.builder().id(1).name("John dummyText").build();

            stubFor(post("/external")
                    .withRequestBody(equalToJson(objectMapper.writeValueAsString(request)))
                    .willReturn(okJson(objectMapper.writeValueAsString(response))));
        }

        log.info(wireMockServer.getStubMappings().toString());
    }

}
