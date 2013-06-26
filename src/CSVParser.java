import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;

public class CSVParser {

	private String path;
	private CSVReader csvReader;
	private ArrayList<String> toAddresses;

	public final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public CSVParser(String path) throws IOException {
		this.path = path;

		toAddresses = new ArrayList<String>();

		csvReader = new CSVReader(new FileReader(this.path));

		parseCSV();
	}

	private void parseCSV() throws IOException {
		String[] nextLine = null;
		while ((nextLine = csvReader.readNext()) != null) {
			if (nextLine[0].equals("emails")) {

			} else {

				String emailRaw = nextLine[0];

				Pattern pattern = Pattern.compile(EMAIL_REGEX);
				Matcher matcher = pattern.matcher(emailRaw);

				if (matcher.matches()) {
					System.out.println(emailRaw);
					toAddresses.add(emailRaw);
				} else {
					String emailSearch = "("
							+ EMAIL_REGEX
									.substring(1, EMAIL_REGEX.length() - 1)
							+ ")" + ".*";

					Pattern pattern2 = Pattern.compile(emailSearch);
					Matcher matcher2 = pattern2.matcher(emailRaw);

					if (matcher2.find()) {

						System.out.println("email corrected");
						System.out.println(matcher2.group(1));
						toAddresses.add(matcher2.group(1));
					}
				}
			}
		}
		System.out
				.println("CSVParser: " + toAddresses.size() + " emails found");
	}

	public ArrayList<String> getToAddresses() {
		return toAddresses;
	}
}
