import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.SwingWorker;

public class EmailController {

	private Properties properties;
	private Authenticator auth;
	private Session session;
	private ArrayList<String> toAddresses, subjects, messages;
	private HashMap<String, String> realName;

	private int progress;
	private int total;

	private String emailTemplate, userName;

	private boolean companyCustomization;

	public EmailController(String host, String port, String userName,
			String password, ArrayList<String> toAddresses,
			boolean companyCustomization) throws InterruptedException,
			ExecutionException, IOException {

		System.out.println("Email Controller Called");

		properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);

		this.toAddresses = toAddresses;
		this.userName = userName;
		subjects = new ArrayList<String>();
		messages = new ArrayList<String>();

		total = this.toAddresses.size();
		progress = 0;

		this.companyCustomization = companyCustomization;

		System.out.println("Parsing Email Template");

		emailTemplate = parseEmailTemplate();
		if (emailTemplate.equals("notfound")) {
			System.out
					.println("Email Template Not Found. Using Default Email Template.");
			emailTemplate = getDefaultTemplate();
		} else {
			System.out.println("Email Template Parse Successful");
		}

		realName = new HashMap<String, String>();
		realName.put("anna@chinacadservices.com", "Anna");
		realName.put("joanna@chinacadservices.com", "Joanna");
		realName.put("jenny@chinacadservices.com", "Jenny");
		realName.put("jeanette@chinacadservices.com", "Jeanette");
		realName.put("info@chinacadservices.com", "Joanna");
		buildSubjectsMessages();

		auth = new SMTPAuthenticator(userName, password);
		session = Session.getInstance(properties, auth);

		System.out.println("Session created");
	}

	public void run() throws InterruptedException, ExecutionException {
		if (toAddresses.size() == subjects.size()
				&& toAddresses.size() == messages.size()) {

			ArrayList<SendWorker> sendWorkers = new ArrayList<SendWorker>();

			for (int i = 0; i < toAddresses.size(); i++) {
				SendWorker sendWorker = new SendWorker(userName,
						toAddresses.get(i), subjects.get(i), messages.get(i));

				sendWorkers.add(sendWorker);
				sendWorker.execute();
			}

			for (SendWorker sendWorker : sendWorkers) {
				if (sendWorker.get() == true) {
					progress++;
					System.out.println(userName + ":\t" + progress + " / "
							+ total);
				}
			}
		}
	}

	private void buildSubjectsMessages() {
		for (String toAddress : toAddresses) {
			String company = parseSubject(toAddress);

			if (companyCustomization == true) {
				subjects.add("CAD Services for " + company);
			} else {
				subjects.add("CAD Services by China CAD Services Inc.");
			}

			String message = emailTemplate;
			if (companyCustomization == true) {
				message = message.replaceAll("COMPANY_NAME", company);
			} else {
				message = message.replaceAll(" from COMPANY_NAME", "");
			}
			message = message.replaceAll("SENDER_NAME",
					realName.get(properties.getProperty("mail.user")));
			messages.add(message);
		}
	}

	private String parseEmailTemplate() throws IOException {
		FileReader fileReader;
		try {
			fileReader = new FileReader("email_template.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String template = "";

			String nextLine;
			while ((nextLine = bufferedReader.readLine()) != null) {
				template = template + nextLine + "\n";
			}
			fileReader.close();
			return template;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "notfound";
		}

	}

	private String parseSubject(String email) {
		Pattern pattern = Pattern.compile(".*@(.*)\\.");
		Matcher matcher = pattern.matcher(email);

		if (matcher.find()) {
			return matcher.group(1).toUpperCase();
		} else {
			return "error";
		}
	}

	public class SendWorker extends SwingWorker<Boolean, Void> {

		private String userName;
		private String toAddress;
		private String subject;
		private String message;

		public SendWorker(String userName, String toAddress, String subject,
				String message) {

			System.out.println("SendWorker called");

			this.userName = userName;
			this.toAddress = toAddress;
			this.subject = subject;
			this.message = message;
		}

		@Override
		protected Boolean doInBackground() throws MessagingException {

			MimeMessage msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(userName));
			InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
			msg.setRecipients(Message.RecipientType.TO, toAddresses);
			msg.setSubject(subject);
			msg.setSentDate(new Date());

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(message, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			msg.setContent(multipart);

			System.out.println(properties.getProperty("mail.user") + "\t"
					+ " sending message to " + "\t" + toAddress);

			try {
				Transport.send(msg);
			} catch (AddressException e) {
				System.out.println(toAddress + " has Address Exception.");
			} catch (SendFailedException e) {
				System.out.println(toAddress + " has Send Failed Exception");
			}

			return true;
		}
	}

	private class SMTPAuthenticator extends Authenticator {
		private String userName;
		private String password;

		public SMTPAuthenticator(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}

	private String getDefaultTemplate() {
		String defaultTemplate = "<p>Dear customer from COMPANY_NAME,</p>\n<p>We are <a title=\"China CAD Services\" href=\"http://www.chinacadservices.com\" target=\"_blank\">China CAD Services Inc</a>, the leading CAD provider in China. We offer fast and cost competitive services that you will love once you\'ve tried us.</p>\n<p><strong><a title=\"Services\" href=\"http://www.chinacadservices.com/services.html\" target=\"_blank\">Services</a>:</strong></p>\n<ul>\n<li>Fast and professional services. (Of course we speak English)</li>\n<li>3D modeling services in SoildWorks, AutoCAD Architecture and Revit</li>\n<li>Overnight, 3, 5, 10, 30-Day Turnarounds</li>\n<li>Fixed Price Per Sheet (Any Content)</li>\n<li>No hourly rate no hidden fees</li>\n<li>Guaranteed Quality</li>\n<li>100% Dimensionally Accurate</li>\n<li>AutoCAD or Microstation</li>\n<li>German Layering names and no Language problem when you open DWG/DGN files, so it look like the jobs are done inside German.</li>\n<li>3-round Quality Control Procedures</li>\n</ul>\n<p><strong><a title=\"Price List\" href=\"http://www.chinacadservices.com/price-list.html\" target=\"_blank\">Prices</a>:</strong></p>\n\n<table border=\"1\">\n<tbody>\n<tr>\n<td><strong>Sheet Size</strong></td>\n<td><strong>Price Per Sheet</strong></td>\n</tr>\n<tr>\n<td>A4</td>\n<td>EUR 10.00</td>\n\n</tr>\n<tr>\n<td>A3</td>\n<td>EUR 20.00</td>\n</tr>\n<tr>\n<td>A2</td>\n<td>EUR 36.00</td>\n</tr>\n<tr>\n<td>A1</td>\n<td>EUR 40.00</td>\n</tr>\n<tr>\n<td>A0</td>\n<td>EUR 50.00</td>\n</tr>\n</tbody>\n</table>\n<p><a title=\"Technologies\" href=\"http://www.chinacadservices.com/services.html\"><strong>Technologies</strong></a>:</p>\n<ul>\n<li>Basic CAD</li>\n<ul>\n<li>Dimensionally accurate CAD drafting</li>\n<li>AIA Layering Standards</li>\n<li>Matches original documents exactly</li>\n</ul>\n<li>Markup Docs</li>\n<ul>\n<li>Paste-ups, redlines, sketches</li>\n<li>Custom layering, fonts, title blocks</li>\n<li>Design development documents</li>\n</ul>\n<li>Custom Services</li>\n<ul>\n<li>Custom layering standards, fonts, backgrounds, blocks, title blocks</li>\n<li>Matches original documents exactly</li>\n</ul>\n<li>Maps and Topos</li>\n<ul>\n<li>Custom layering standards</li>\n<li>Overly original documents exactly</li>\n<li>True CAD text</li>\n</ul>\n<li>3D Modeling Services</li>\n<ul>\n<li>SolidWorks</li>\n<li>AutoCAD Architecture</li>\n<li>Revit</li>\n</ul>\n<li>Other Services</li>\n<ul>\n<li><a title=\"Price List\" href=\"http://www.chinacadservices.com/price-list.html\" target=\"_blank\">Check out our full listing here.</a></li>\n</ul>\n</ul>\n<p><br />Simply reply to this email for a free sample, no strings attached.</p>\n<p>We look forward to your business.</p>\n<p>&nbsp;</p>\n<p><strong>SENDER_NAME</strong></p>\n<p>Co-Founder</p>\n<p><a title=\"china cad\" href=\"http://www.chinacadservices.com\" target=\"_blank\">China CAD Services Inc.</a></p>\n<p>11-34, Chuanshen Rd., Shanghai, China</p>";

		return defaultTemplate;
	}
}
