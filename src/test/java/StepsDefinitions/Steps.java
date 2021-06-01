package StepsDefinitions;

import org.testng.Assert;

import apiEngine.Endpoints;
import apiEngine.IRestResponse;
import apiEngine.model.requests.AddBookRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.ISBN;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.responses.Book;
import apiEngine.model.responses.Books;
import apiEngine.model.responses.Token;
import apiEngine.model.responses.UserAccount;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class Steps {
	private static final String userID = "027b1b4a-91f3-4403-9a7e-1312fe9db040";
	private static final String userName = "raju";
	private static final String password = "Test@1234";
	private static Response response;
	private static Token tokenResponse; 
	private static Book book;
	private static IRestResponse<UserAccount> userAccountResponse;
    private final String BaseUrl = "https://bookstore.toolsqa.com";
    private Endpoints endPoints;

	@Given("I am an authorized user")
	public void i_am_an_authorized_user() {
		endPoints = new Endpoints(BaseUrl);
		AuthorizationRequest credentials = new AuthorizationRequest(userName, password);
		endPoints.authenticateUser(credentials);
	}

	@Given("A list of books are available")
	public void a_list_of_books_are_available() {
		IRestResponse<Books> booksResponse = endPoints.getBooks();
		book = booksResponse.getBody().books.get(0); 
	}

	@When("I add a book to my reading list")
	public void i_add_a_book_to_my_reading_list() {
        ISBN isbn = new ISBN(book.isbn);
        AddBookRequest addBooksRequest = new AddBookRequest(userID, isbn);
        userAccountResponse = endPoints.addBook(addBooksRequest);
	}

	@Then("the book is added")
	public void the_book_is_added() {
		Assert.assertTrue(userAccountResponse.isSuccessful());
		Assert.assertEquals(201, userAccountResponse.getStatusCode());

		Assert.assertEquals(book.isbn, userAccountResponse.getBody().books.get(0).isbn);
	}

	@When("I remove a book from my reading list")
	public void removeBookFromList() {
        RemoveBookRequest removeBookRequest = new RemoveBookRequest(userID, book.isbn);
        response = endPoints.removeBook(removeBookRequest);
	}

	@Then("The book is removed")
	public void bookIsRemoved() {
		Assert.assertEquals(204, response.getStatusCode());
		
		userAccountResponse = endPoints.getUserAccount(userID);
		Assert.assertEquals(200, userAccountResponse.getStatusCode());
		Assert.assertEquals(0, userAccountResponse.getBody().books.size());
	}

}
