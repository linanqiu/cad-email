import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JCheckBox;

public class EmailGUI {

	private JFrame frame;
	private JTextField txtFilepath;
	private JTextField txtEmailAddress;
	private JTextField txtTotalEmail;
	private JTextField txtProgress;
	private JTextField txtEmailAddress_1;
	private JTextField txtTotalEmail_1;
	private JTextField txtProgress_1;
	private JTextField txtEmailAddress_2;
	private JTextField txtTotalEmail_2;
	private JTextField txtProgress_2;
	private JTextField txtEmailAddress_3;
	private JTextField txtTotalEmail_3;
	private JTextField txtProgress_3;
	private JTextField txtPassword;
	private JButton btnLoadCsv;
	private JButton btnFireTheEmails;
	private JTextArea txtrConsole;

	public final int RELAY_LIMIT_DEFAULT = 200;
	public final int RELAY_LIMIT_MAXIMUM = 220;

	public final String EMAIL_1 = "anna@chinacadservices.com";
	public final String EMAIL_2 = "joanna@chinacadservices.com";
	public final String EMAIL_3 = "jeanette@chinacadservices.com";
	public final String EMAIL_4 = "jenny@chinacadservices.com";

	private CSVParser csvParser;

	private ArrayList<String> toAddresses;
	private ArrayList<String> toAddresses1;
	private ArrayList<String> toAddresses2;
	private ArrayList<String> toAddresses3;
	private ArrayList<String> toAddresses4;
	private JCheckBox chckbxCustomizeCompanyNames;
	private JTextField txtRelayLimit;
	private JLabel lblRelayLimit;
	private JLabel lblPort;
	private JTextField txtPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmailGUI window = new EmailGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmailGUI() {
		// redirectSystemStreams();

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 742, 541);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		txtFilepath = new JTextField();
		txtFilepath.setText("filePath");
		GridBagConstraints gbc_txtFilepath = new GridBagConstraints();
		gbc_txtFilepath.insets = new Insets(0, 0, 5, 5);
		gbc_txtFilepath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFilepath.gridx = 0;
		gbc_txtFilepath.gridy = 0;
		frame.getContentPane().add(txtFilepath, gbc_txtFilepath);
		txtFilepath.setColumns(10);

		btnLoadCsv = new JButton("Load CSV");
		GridBagConstraints gbc_btnLoadCsv = new GridBagConstraints();
		gbc_btnLoadCsv.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadCsv.gridx = 1;
		gbc_btnLoadCsv.gridy = 0;
		frame.getContentPane().add(btnLoadCsv, gbc_btnLoadCsv);

		lblRelayLimit = new JLabel("Relay Limit:");
		GridBagConstraints gbc_lblRelayLimit = new GridBagConstraints();
		gbc_lblRelayLimit.insets = new Insets(0, 0, 5, 5);
		gbc_lblRelayLimit.anchor = GridBagConstraints.EAST;
		gbc_lblRelayLimit.gridx = 0;
		gbc_lblRelayLimit.gridy = 1;
		frame.getContentPane().add(lblRelayLimit, gbc_lblRelayLimit);

		txtRelayLimit = new JTextField();
		txtRelayLimit.setText("200");
		GridBagConstraints gbc_txtRelayLimit = new GridBagConstraints();
		gbc_txtRelayLimit.insets = new Insets(0, 0, 5, 0);
		gbc_txtRelayLimit.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRelayLimit.gridx = 1;
		gbc_txtRelayLimit.gridy = 1;
		frame.getContentPane().add(txtRelayLimit, gbc_txtRelayLimit);
		txtRelayLimit.setColumns(10);

		lblPort = new JLabel("Port:");
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 0;
		gbc_lblPort.gridy = 2;
		frame.getContentPane().add(lblPort, gbc_lblPort);

		txtPort = new JTextField();
		txtPort.setText("3535");
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.gridx = 1;
		gbc_txtPort.gridy = 2;
		frame.getContentPane().add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblEmailAddress = new JLabel("Email Address");
		GridBagConstraints gbc_lblEmailAddress = new GridBagConstraints();
		gbc_lblEmailAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmailAddress.gridx = 0;
		gbc_lblEmailAddress.gridy = 0;
		panel.add(lblEmailAddress, gbc_lblEmailAddress);

		JLabel lblTotalEmails = new JLabel("Total Emails");
		GridBagConstraints gbc_lblTotalEmails = new GridBagConstraints();
		gbc_lblTotalEmails.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalEmails.gridx = 1;
		gbc_lblTotalEmails.gridy = 0;
		panel.add(lblTotalEmails, gbc_lblTotalEmails);

		JLabel lblProgress = new JLabel("Progress");
		GridBagConstraints gbc_lblProgress = new GridBagConstraints();
		gbc_lblProgress.insets = new Insets(0, 0, 5, 0);
		gbc_lblProgress.gridx = 2;
		gbc_lblProgress.gridy = 0;
		panel.add(lblProgress, gbc_lblProgress);

		txtEmailAddress = new JTextField();
		txtEmailAddress.setText("Email Address 1");
		GridBagConstraints gbc_txtEmailAddress = new GridBagConstraints();
		gbc_txtEmailAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtEmailAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmailAddress.gridx = 0;
		gbc_txtEmailAddress.gridy = 1;
		panel.add(txtEmailAddress, gbc_txtEmailAddress);
		txtEmailAddress.setColumns(10);

		txtTotalEmail = new JTextField();
		txtTotalEmail.setEditable(false);
		txtTotalEmail.setText("Total Email 1");
		GridBagConstraints gbc_txtTotalEmail = new GridBagConstraints();
		gbc_txtTotalEmail.insets = new Insets(0, 0, 5, 5);
		gbc_txtTotalEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalEmail.gridx = 1;
		gbc_txtTotalEmail.gridy = 1;
		panel.add(txtTotalEmail, gbc_txtTotalEmail);
		txtTotalEmail.setColumns(10);

		txtProgress = new JTextField();
		txtProgress.setEditable(false);
		txtProgress.setText("Progress 1");
		GridBagConstraints gbc_txtProgress = new GridBagConstraints();
		gbc_txtProgress.insets = new Insets(0, 0, 5, 0);
		gbc_txtProgress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProgress.gridx = 2;
		gbc_txtProgress.gridy = 1;
		panel.add(txtProgress, gbc_txtProgress);
		txtProgress.setColumns(10);

		txtEmailAddress_1 = new JTextField();
		txtEmailAddress_1.setText("Email Address 2");
		GridBagConstraints gbc_txtEmailAddress_1 = new GridBagConstraints();
		gbc_txtEmailAddress_1.insets = new Insets(0, 0, 5, 5);
		gbc_txtEmailAddress_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmailAddress_1.gridx = 0;
		gbc_txtEmailAddress_1.gridy = 2;
		panel.add(txtEmailAddress_1, gbc_txtEmailAddress_1);
		txtEmailAddress_1.setColumns(10);

		txtTotalEmail_1 = new JTextField();
		txtTotalEmail_1.setEditable(false);
		txtTotalEmail_1.setText("Total Email 2");
		GridBagConstraints gbc_txtTotalEmail_1 = new GridBagConstraints();
		gbc_txtTotalEmail_1.insets = new Insets(0, 0, 5, 5);
		gbc_txtTotalEmail_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalEmail_1.gridx = 1;
		gbc_txtTotalEmail_1.gridy = 2;
		panel.add(txtTotalEmail_1, gbc_txtTotalEmail_1);
		txtTotalEmail_1.setColumns(10);

		txtProgress_1 = new JTextField();
		txtProgress_1.setEditable(false);
		txtProgress_1.setText("Progress 2");
		GridBagConstraints gbc_txtProgress_1 = new GridBagConstraints();
		gbc_txtProgress_1.insets = new Insets(0, 0, 5, 0);
		gbc_txtProgress_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProgress_1.gridx = 2;
		gbc_txtProgress_1.gridy = 2;
		panel.add(txtProgress_1, gbc_txtProgress_1);
		txtProgress_1.setColumns(10);

		txtEmailAddress_2 = new JTextField();
		txtEmailAddress_2.setText("Email Address 3");
		GridBagConstraints gbc_txtEmailAddress_2 = new GridBagConstraints();
		gbc_txtEmailAddress_2.insets = new Insets(0, 0, 5, 5);
		gbc_txtEmailAddress_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmailAddress_2.gridx = 0;
		gbc_txtEmailAddress_2.gridy = 3;
		panel.add(txtEmailAddress_2, gbc_txtEmailAddress_2);
		txtEmailAddress_2.setColumns(10);

		txtTotalEmail_2 = new JTextField();
		txtTotalEmail_2.setEditable(false);
		txtTotalEmail_2.setText("Total Email 3");
		GridBagConstraints gbc_txtTotalEmail_2 = new GridBagConstraints();
		gbc_txtTotalEmail_2.insets = new Insets(0, 0, 5, 5);
		gbc_txtTotalEmail_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalEmail_2.gridx = 1;
		gbc_txtTotalEmail_2.gridy = 3;
		panel.add(txtTotalEmail_2, gbc_txtTotalEmail_2);
		txtTotalEmail_2.setColumns(10);

		txtProgress_2 = new JTextField();
		txtProgress_2.setEditable(false);
		txtProgress_2.setText("Progress 3");
		GridBagConstraints gbc_txtProgress_2 = new GridBagConstraints();
		gbc_txtProgress_2.insets = new Insets(0, 0, 5, 0);
		gbc_txtProgress_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProgress_2.gridx = 2;
		gbc_txtProgress_2.gridy = 3;
		panel.add(txtProgress_2, gbc_txtProgress_2);
		txtProgress_2.setColumns(10);

		txtEmailAddress_3 = new JTextField();
		txtEmailAddress_3.setText("Email Address 4");
		GridBagConstraints gbc_txtEmailAddress_3 = new GridBagConstraints();
		gbc_txtEmailAddress_3.insets = new Insets(0, 0, 0, 5);
		gbc_txtEmailAddress_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmailAddress_3.gridx = 0;
		gbc_txtEmailAddress_3.gridy = 4;
		panel.add(txtEmailAddress_3, gbc_txtEmailAddress_3);
		txtEmailAddress_3.setColumns(10);

		txtTotalEmail_3 = new JTextField();
		txtTotalEmail_3.setEditable(false);
		txtTotalEmail_3.setText("Total Email 4");
		GridBagConstraints gbc_txtTotalEmail_3 = new GridBagConstraints();
		gbc_txtTotalEmail_3.insets = new Insets(0, 0, 0, 5);
		gbc_txtTotalEmail_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalEmail_3.gridx = 1;
		gbc_txtTotalEmail_3.gridy = 4;
		panel.add(txtTotalEmail_3, gbc_txtTotalEmail_3);
		txtTotalEmail_3.setColumns(10);

		txtProgress_3 = new JTextField();
		txtProgress_3.setEditable(false);
		txtProgress_3.setText("Progress 4");
		GridBagConstraints gbc_txtProgress_3 = new GridBagConstraints();
		gbc_txtProgress_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProgress_3.gridx = 2;
		gbc_txtProgress_3.gridy = 4;
		panel.add(txtProgress_3, gbc_txtProgress_3);
		txtProgress_3.setColumns(10);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 4;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 0;
		panel_1.add(lblPassword, gbc_lblPassword);

		txtPassword = new JTextField();
		txtPassword.setEnabled(false);
		txtPassword.setText("Password");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 0;
		panel_1.add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);

		chckbxCustomizeCompanyNames = new JCheckBox(
				"Customize Company Names Based on Emails");
		chckbxCustomizeCompanyNames.setEnabled(false);
		chckbxCustomizeCompanyNames.setSelected(true);
		GridBagConstraints gbc_chckbxCustomizeCompanyNames = new GridBagConstraints();
		gbc_chckbxCustomizeCompanyNames.gridwidth = 2;
		gbc_chckbxCustomizeCompanyNames.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxCustomizeCompanyNames.gridx = 0;
		gbc_chckbxCustomizeCompanyNames.gridy = 1;
		panel_1.add(chckbxCustomizeCompanyNames,
				gbc_chckbxCustomizeCompanyNames);

		btnFireTheEmails = new JButton("Fire The Emails");
		btnFireTheEmails.setEnabled(false);
		GridBagConstraints gbc_btnFireTheEmails = new GridBagConstraints();
		gbc_btnFireTheEmails.gridwidth = 2;
		gbc_btnFireTheEmails.gridx = 0;
		gbc_btnFireTheEmails.gridy = 2;
		panel_1.add(btnFireTheEmails, gbc_btnFireTheEmails);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);

		txtrConsole = new JTextArea();
		txtrConsole.setText("Console");
		scrollPane.setViewportView(txtrConsole);

		btnLoadCsv.addActionListener(new FileChooserListener());
		btnFireTheEmails.addActionListener(new FireListener());

		txtEmailAddress.setEnabled(false);
		txtEmailAddress_1.setEnabled(false);
		txtEmailAddress_2.setEnabled(false);
		txtEmailAddress_3.setEnabled(false);

		txtEmailAddress.setText("Not Used");
		txtEmailAddress_1.setText("Not Used");
		txtEmailAddress_2.setText("Not Used");
		txtEmailAddress_3.setText("Not Used");
	}

	public class FireListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String host = "smtpout.asia.secureserver.net";
			String port = txtPort.getText();
			String password = txtPassword.getText();
			boolean companyCustomization = false;

			if (chckbxCustomizeCompanyNames.isSelected()) {
				companyCustomization = true;
			} else {
				companyCustomization = false;
			}

			if (toAddresses1.size() > 0) {
				if (emailCheck(txtEmailAddress.getText())) {
					FireWorker fireWorker = new FireWorker(host, port,
							txtEmailAddress.getText().toLowerCase(), password,
							toAddresses1, companyCustomization, txtProgress);
					txtProgress.setText("Sending Emails");
					fireWorker.execute();
					try {
						fireWorker.get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					txtEmailAddress.setText("Invalid Email");
				}
			}

			if (toAddresses2.size() > 0) {
				if (emailCheck(txtEmailAddress_1.getText())) {
					FireWorker fireWorker = new FireWorker(host, port,
							txtEmailAddress_1.getText().toLowerCase(),
							password, toAddresses2, companyCustomization,
							txtProgress_1);
					txtProgress_1.setText("Sending Emails");
					fireWorker.execute();
					try {
						fireWorker.get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					txtEmailAddress_1.setText("Invalid Email");
				}
			}

			if (toAddresses3.size() > 0) {
				if (emailCheck(txtEmailAddress_2.getText())) {
					FireWorker fireWorker = new FireWorker(host, port,
							txtEmailAddress_2.getText().toLowerCase(),
							password, toAddresses3, companyCustomization,
							txtProgress_2);
					txtProgress_2.setText("Sending Emails");
					fireWorker.execute();
					try {
						fireWorker.get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					txtEmailAddress_2.setText("Invalid Email");
				}
			}

			if (toAddresses4.size() > 0) {
				if (emailCheck(txtEmailAddress_3.getText())) {
					FireWorker fireWorker = new FireWorker(host, port,
							txtEmailAddress_3.getText().toLowerCase(),
							password, toAddresses4, companyCustomization,
							txtProgress_3);
					txtProgress_3.setText("Sending Emails");
					fireWorker.execute();
					try {
						fireWorker.get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					txtEmailAddress_3.setText("Invalid Email");
				}
			}
		}

		private boolean emailCheck(String email) {

			Pattern pattern = Pattern.compile(".*\\@.*\\..*");
			Matcher matcher = pattern.matcher(email);

			if (matcher.find()) {
				return true;
			} else {
				return false;
			}
		}

	}

	public class FireWorker extends SwingWorker<Void, Void> {

		private String host;
		private String port;
		private String userName;
		private String password;
		private ArrayList<String> toAddresses;
		boolean companyCustomization;
		private JTextField txtProgress;

		public FireWorker(String host, String port, String userName,
				String password, ArrayList<String> toAddresses,
				boolean companyCustomization, JTextField txtProgress) {

			this.host = host;
			this.port = port;
			this.userName = userName;
			this.password = password;
			this.toAddresses = toAddresses;
			this.companyCustomization = companyCustomization;
			this.txtProgress = txtProgress;
		}

		@Override
		protected Void doInBackground() throws Exception {

			EmailController emailController = new EmailController(host, port,
					userName, password, toAddresses, companyCustomization);

			emailController.run();

			return null;
		}

		protected void done() {
			txtProgress.setText("Done");
		}
	}

	public class FileChooserListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();

			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File csvFile = fileChooser.getSelectedFile();
				txtFilepath.setText(csvFile.getAbsolutePath());
				try {
					int relayLimit = RELAY_LIMIT_DEFAULT;
					try {
						relayLimit = Integer.valueOf(txtRelayLimit.getText());
					} catch (NumberFormatException e1) {
						txtRelayLimit.setText(Integer
								.toString(RELAY_LIMIT_DEFAULT));
						relayLimit = RELAY_LIMIT_DEFAULT;
					}
					if (relayLimit > RELAY_LIMIT_MAXIMUM) {
						relayLimit = RELAY_LIMIT_DEFAULT;
						txtRelayLimit.setText(Integer
								.toString(RELAY_LIMIT_MAXIMUM));
					} else if (relayLimit < 0) {
						relayLimit = RELAY_LIMIT_DEFAULT;
						txtRelayLimit.setText(Integer
								.toString(RELAY_LIMIT_DEFAULT));
					}

					csvParser = new CSVParser(txtFilepath.getText());
					toAddresses = csvParser.getToAddresses();

					toAddresses1 = new ArrayList<String>();
					toAddresses2 = new ArrayList<String>();
					toAddresses3 = new ArrayList<String>();
					toAddresses4 = new ArrayList<String>();

					ArrayList<String> emailsRandomized = randomizeEmails();

					boolean oversized = false;
					for (int i = 0; i < toAddresses.size(); i++) {
						if (i < relayLimit) {
							toAddresses1.add(toAddresses.get(i));
							txtEmailAddress.setEnabled(true);
							txtEmailAddress.setText(emailsRandomized.get(0));

						} else if (i < relayLimit * 2) {
							toAddresses2.add(toAddresses.get(i));
							txtEmailAddress_1.setEnabled(true);
							txtEmailAddress_1.setText(emailsRandomized.get(1));

						} else if (i < relayLimit * 3) {
							toAddresses3.add(toAddresses.get(i));
							txtEmailAddress_2.setEnabled(true);
							txtEmailAddress_2.setText(emailsRandomized.get(2));

						} else if (i < relayLimit * 4) {
							toAddresses4.add(toAddresses.get(i));
							txtEmailAddress_3.setEnabled(true);
							txtEmailAddress_3.setText(emailsRandomized.get(3));

						} else if (i >= relayLimit * 4) {
							oversized = true;
						}
					}

					if (oversized) {
						System.out.println("CSV contains more than "
								+ (relayLimit * 4)
								+ " emails. Only using first "
								+ (relayLimit * 4) + " emails.");
					}

					txtTotalEmail.setText(String.valueOf(toAddresses1.size()));
					txtTotalEmail_1
							.setText(String.valueOf(toAddresses2.size()));
					txtTotalEmail_2
							.setText(String.valueOf(toAddresses3.size()));
					txtTotalEmail_3
							.setText(String.valueOf(toAddresses4.size()));

					txtProgress.setText("Not Started");
					txtProgress_1.setText("Not Started");
					txtProgress_2.setText("Not Started");
					txtProgress_3.setText("Not Started");

					System.out
							.println(toAddresses.size() + " emails retrieved");

					txtPassword.setEnabled(true);
					btnFireTheEmails.setEnabled(true);
					chckbxCustomizeCompanyNames.setEnabled(true);
					txtRelayLimit.setEnabled(false);

				} catch (IOException e1) {
					txtFilepath.setText("Invalid CSV File.");
				}
			}
		}
	}

	private ArrayList<String> randomizeEmails() {

		ArrayList<String> emails = new ArrayList<String>();

		emails.add(EMAIL_1);
		emails.add(EMAIL_2);
		emails.add(EMAIL_3);
		emails.add(EMAIL_4);

		Collections.shuffle(emails);

		return emails;
	}

	private void redirectSystemStreams() {
		OutputStream out = new ConsoleOutputStream();

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

	private class ConsoleOutputStream extends OutputStream {

		private void updateTextArea(String text) {
			SwingUtilities.invokeLater(new updateTextRunnable(text));
		}

		@Override
		public void write(int arg0) throws IOException {
			updateTextArea(String.valueOf((char) arg0));
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			updateTextArea(new String(b, off, len));
		}

		@Override
		public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
		}

		private class updateTextRunnable implements Runnable {

			String text;

			public updateTextRunnable(String text) {
				this.text = text;
			}

			@Override
			public void run() {
				txtrConsole.append(text);
			}

		}

	}
}
