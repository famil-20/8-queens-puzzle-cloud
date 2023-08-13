package queenspuzzle;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import queenspuzzle.Queen;
import queenspuzzle.Board;
import queenspuzzle.Solver;

public class MainHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        try {
            System.out.println(event);
            String reqBody = event.getBody(); // input is provided in the request body
            System.out.println(reqBody);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(reqBody);

            int input = jsonNode.get("input").asInt();

            Queen[] queens = new Queen[input];
            Board x = new Board(input);
            for (int i = 0; i < queens.length; i++) {
                queens[i] = new Queen("Queen", x, i, 0);
            }
            Solver a = new Solver(queens, queens.length);
            String result = a.solve();

            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            response.setBody(result);

            return response;
        } catch (Exception e) {
            System.err.println(e);
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(500);
            response.setBody("Internal Server error");
            return response;
        }
    }
}
