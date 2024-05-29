package studia;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class CurrencyConverterResourceTest {


    @Test
    public void testGetCurrenciesEndpoint() {
        given()
                .when().get("/converter/currencies")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", is(3))
                .body("[0]", is("USD"))
                .body("[1]", is("EUR"))
                .body("[2]", is("PLN"));
    }

    @Test
    public void testConvertEndpointInvalidCurrency() {
        String requestBody = "{\"amount\": 100, \"from\": \"USD\", \"to\": \"XYZ\"}";

        given()
                .body(requestBody)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when().post("/converter")
                .then()
                .statusCode(400)
                .body(is("Unsupported currency conversion: USD to XYZ"));
    }
}