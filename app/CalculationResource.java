package app;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Calculation;

import java.util.HashMap;
import java.util.Map;

@Path("/api/calc")
public class CalculationResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCalculation(CalculationRequest request) {
        try {
            int number1 = request.getNumber1();
            int number2 = request.getNumber2();
            String operation = request.getOperation();
            double result;

            switch (operation) {
                case "+":
                    result = number1 + number2;
                    break;
                case "-":
                    result = number1 - number2;
                    break;
                case "*":
                    result = number1 * number2;
                    break;
                case "/":
                    if (number2 == 0) {
                        throw new IllegalArgumentException("Division by zero is not allowed.");
                    }
                    result = number1 / (double) number2;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation type. Use +, -, *, or /.");
            }

            // Store the calculation in the database
            Calculation calculation = new Calculation(number1, number2, operation, result);
         

            Map<String, Object> response = new HashMap<>();
            response.put("Result", result);

            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An error occurred: " + e.getMessage())
                .build();
        }
    }
}
