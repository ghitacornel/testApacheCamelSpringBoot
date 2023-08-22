package camel;

import camel.route.SimpleRoute;
import lombok.SneakyThrows;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class SimpleRouteTest extends CamelTestSupport {

    @Override
    public RoutesBuilder createRouteBuilder() {
        return new SimpleRoute();
    }

    @SneakyThrows
    @Test
    public void testRoute() {

//        MockEndpoint mock = getMockEndpoint("bean:finalStep");
//        mock.expectedBodiesReceived("expected message");

        String input = "Hello";
        template.sendBody("direct:start", input);

//        mock.assertIsSatisfied();

    }
}
