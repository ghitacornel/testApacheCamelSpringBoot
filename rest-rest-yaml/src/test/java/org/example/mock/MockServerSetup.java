package org.example.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

//@WireMockTest
// doesn't work, need to investigate, till then just manual setup
public abstract class MockServerSetup {

    static final WireMockServer wireMockServer = new WireMockServer(8080, 8181);

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
            stubFor(get("/external/hello").willReturn(ok("Hello World " + LocalDateTime.now())));
        }
    }

}
