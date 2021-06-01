package apiEngine;

import org.apache.http.HttpStatus;

import apiEngine.model.requests.AddBookRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.responses.Books;
import apiEngine.model.responses.Token;
import apiEngine.model.responses.UserAccount;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Endpoints {	
	private final RequestSpecification request;
	
	public Endpoints(String BASE_URL) {
		RestAssured.baseURI = BASE_URL;
	    request = RestAssured.given();
		request.header("Content-Type", "application/json");
	}
	
	public void authenticateUser(AuthorizationRequest authRequest) {
	       Response response = request.body(authRequest).post(Route.generateToken());
	        if (response.statusCode() != HttpStatus.SC_OK)
	            throw new RuntimeException("Authentication Failed. Content of failed Response: " + response.toString() + " , Status Code : " + response.statusCode());
	 
	        Token tokenResponse = response.body().jsonPath().getObject("$", Token.class);
	        request.header("Authorization", "Bearer " + tokenResponse.token);
	}
	
	public IRestResponse<Books> getBooks() {
		Response response = request.get(Route.books());
		return new RestResponse<Books>(Books.class, response);
	}
	
	public IRestResponse<UserAccount> addBook(AddBookRequest addBooksRequest) {
		Response response = request.body(addBooksRequest).post(Route.books());
		return new RestResponse<UserAccount>(UserAccount.class, response);
	}
	
	public Response removeBook(RemoveBookRequest removeBookRequest) {
		return request.body(removeBookRequest).delete(Route.book());
	}
	
    public IRestResponse<UserAccount> getUserAccount(String userId) {
        Response response = request.get(Route.userAccount(userId));
        return new RestResponse<UserAccount>(UserAccount.class, response);
    }
}
