import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EmailControllerTest {

	/**
	 * @param args
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,
			ExecutionException, IOException {
		System.out.println("Test Class Called");

		String host = "smtpout.asia.secureserver.net";
		String port = "3535";
		String userName = "anna@chinacadservices.com";
		String password = "98DTech98DTech";

		CSVParser csvParser = new CSVParser("emails.csv");

		EmailController emailController = new EmailController(host, port,
				userName, password, csvParser.getToAddresses(), false);
	}

}
