package APITest;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class E2E_Tests {
	//generate token
	public static void main(String args[]) {
		String userID = "5f796e09-ba38-416a-b385-44d5982292fd";
        String userName = "sume01";
        String password = "Test@1234";
        String baseUrl = "https://bookstore.toolsqa.com";
 
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        
        request.header("Content-Type", "application/json");
        Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
        		.post("/Account/v1/GenerateToken");
        
        Assert.assertEquals(response.getStatusCode(), 200);
        String jsonString = response.asString();
        String token = JsonPath.from(jsonString).get("token");
        System.out.println(token);
        
        response = request.get("/BookStore/v1/Books");
        
        Assert.assertEquals(response.getStatusCode(), 200);
 
        jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        Assert.assertTrue(books.size() > 0);
 
         //This bookId will be used in later requests, to add the book with respective isbn
        String bookId = books.get(0).get("isbn");
        System.out.println(bookId);
        
        request.header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json");

response = request.body("{ \"userId\": \"" + userID + "\", " +
        "\"collectionOfIsbns\": [ { \"isbn\": \"" + bookId + "\" } ]}")
        .post("/BookStore/v1/Books");

Assert.assertEquals( 201, response.getStatusCode());
        
	}

}
