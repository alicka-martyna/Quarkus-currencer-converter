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

//    Testuje endpoint GET /converter/currencies.
//    Sprawdza, czy zwrócony status HTTP to 200.
//    Sprawdza, czy zawartość odpowiedzi jest typu application/json.
//    Sprawdza, czy lista walut ma długość 3 i zawiera USD, EUR i PLN.

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

//    Testuje endpoint POST /converter z poprawnymi danymi wejściowymi.
//    Sprawdza, czy zwrócony status HTTP to 200.
//    Sprawdza, czy zawartość odpowiedzi jest typu application/json.
//    Sprawdza, czy wartości w odpowiedzi są zgodne z oczekiwanymi wartościami dla poprawnej konwersji.
    @Test
    public void testConvertEndpointValid() {
        String requestBody = "{\"amount\": 100, \"from\": \"USD\", \"to\": \"EUR\"}";

        given()
                .body(requestBody)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when().post("/converter")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("originalAmount", is(100.0F))
                .body("fromCurrency", is("USD"))
                .body("toCurrency", is("EUR"))
                .body("convertedAmount", is(notNullValue()));  // Assuming the conversion rate is known and fixed
    }

//  Testuje endpoint POST /converter z niepoprawnymi danymi wejściowymi (nieobsługiwana waluta).
//  Sprawdza, czy zwrócony status HTTP to 400.
//  Sprawdza, czy wiadomość o błędzie jest zgodna z oczekiwaną.
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