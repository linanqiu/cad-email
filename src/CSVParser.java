import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;

public class CSVParser {

	private String path;
	private CSVReader csvReader;
	private ArrayList<String> toAddresses;

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
				toAddresses.add(nextLine[0]);
			}
		}
		System.out
				.println("CSVParser: " + toAddresses.size() + " emails found");
	}

	public ArrayList<String> getToAddresses() {
		return toAddresses;
	}
}
