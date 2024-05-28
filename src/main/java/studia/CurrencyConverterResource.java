package studia;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/converter")
public class CurrencyConverterResource {
    private static final Map<String, Double> exchangeRates = new HashMap<>();
    private static final List<String> supportedCurrencies = Arrays.asList("USD", "EUR", "PLN");

    static {
        exchangeRates.put("USD_EUR", 0.85);
        exchangeRates.put("EUR_USD", 1.18);
        exchangeRates.put("USD_PLN", 4.0);
        exchangeRates.put("PLN_USD", 0.25);
    }

    public static class CurrencyRequest {
        public double amount;
        public String from;
        public String to;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convert(CurrencyRequest request) {
        String key = request.from + "_" + request.to;
        Double rate;

        if (request.from.equals(request.to)) {
            rate = 1.0;
        }
        else{
            rate = exchangeRates.get(key);
        }

        if (rate == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unsupported currency conversion: " + request.from + " to " + request.to)
                    .build();
        }

        double convertedAmount = request.amount * rate;
        ConversionResult result = new ConversionResult(request.amount, request.from, request.to, convertedAmount);
        return Response.ok(result).build();
    }

    public static class ConversionResult {
        public double originalAmount;
        public String fromCurrency;
        public String toCurrency;
        public double convertedAmount;

        public ConversionResult(double originalAmount, String fromCurrency, String toCurrency, double convertedAmount) {
            this.originalAmount = originalAmount;
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.convertedAmount = convertedAmount;
        }
    }

    @GET
    @Path("/currencies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getCurrencies() {
        return supportedCurrencies;
    }

}