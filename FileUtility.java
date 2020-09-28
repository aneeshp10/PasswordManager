// --== CS400 File Header Information ==--
// Name: Aneesh Patil
// Email: apatil6@wisc.edu
// Team: GA
// TA: Daniel Kiel
// Lecturer: Gary Dahl
// Notes to Grader: NA

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This class is used to load and save data from a file to a hashTableMap and
 * its contents
 * 
 * @author barna
 *
 */
public class FileUtility {

	private File file;// The text file where changes and saves will occur
	private Scanner scan = null;
	private boolean isEmpty = true;

	// The constructor takes a file as its parameter
	public FileUtility(File file) {
		Scanner temp;
		try {
			file.createNewFile();
			this.file = file;

			temp = new Scanner(file);
			isEmpty = !temp.hasNext();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * This method loads data from a text file into a HashTableMap and a LinkedList
	 * with the information of the User object
	 * 
	 * @param users           The HashTableMap that stores User objects
	 * @param listOfUsernames The LinkedList that stores usernames
	 */
	public void loadData(HashTableMap<String, User> users, LinkedList<String> listOfUsernames) {
		String loginUsername = null;
		String loginPassword = null;
		String[] urls = null;
		String[] usernames = null;
		String[] passwords = null;

		boolean isCredentialsEmpty = true;

		try {
			scan = new Scanner(file);

			while (scan.hasNext()) {
				String line = scan.nextLine();
				String info;
				if (line.equals("") || line.equals("\n")) {
					continue;
				}

				info = line;
				line = line.substring(0, line.indexOf(":"));

				if (line.equals("LoginUser")) {
					loginUsername = info.substring(info.indexOf(":") + 1).trim();
				}

				if (line.equals("LoginPassword")) {
					loginPassword = info.substring(info.indexOf(":") + 1).trim();
				}

				if (line.equals("Urls")) {
					urls = info.substring(info.indexOf(":") + 1).trim().split("\\s");
					if (urls[0].length() == 0) {
						isCredentialsEmpty = true;
					} else {
						isCredentialsEmpty = false;
					}
				}

				if (line.equals("Usernames") && isCredentialsEmpty == false) {
					usernames = info.substring(info.indexOf(":") + 1).trim().split("\\s");
				}

				if (line.equals("Passwords") && isCredentialsEmpty == false) {

					info = info.substring(info.indexOf(":") + 1).trim();
					// System.out.println(info);
					passwords = new String[urls.length];

					String temp = "";

					boolean onPassword = false;
					int counter = 0;

					for (int i = 0; i < info.length(); i++) {

						if (info.charAt(i) == '\"') {
							if (onPassword == false) {
								onPassword = true;
							} else {
								onPassword = false;
								passwords[counter] = temp.substring(1);
								counter++;
								temp = "";

							}
						}

						if (onPassword == true) {
							temp += info.charAt(i);
						}
					}
				}

				// Add data to HashTableMap
				if (line.contains("stop")) {
					// System.out.println(loginUsername);
					users.put(loginUsername, new User(loginUsername, loginPassword));
					listOfUsernames.add(loginUsername);

					if (isCredentialsEmpty == false) {
						for (int i = 0; i < urls.length; i++) {
							users.get(loginUsername).addCredential(urls[i], usernames[i], passwords[i]);

						}
					}

					loginUsername = null;
					loginPassword = null;
					urls = null;
					usernames = null;
					passwords = null;
					isCredentialsEmpty = true;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scan != null)
				scan.close();
		}
	}

	/**
	 * This functions saves the changes that occured in the HashTableMap of Users
	 * and LinkedList of usernames into the text file
	 * 
	 * @param users           The HashTableMap of Users
	 * @param listOfUsernames LinkedList of usernames
	 */
	public void saveData(HashTableMap<String, User> users, LinkedList<String> listOfUsernames) {
		clearFile();
		for (int i = 0; i < listOfUsernames.size(); i++) {

			User tempUser = users.get(listOfUsernames.get(i));
			writeFile("LoginUser:" + tempUser.getLoginUsername());// .
			writeFile("LoginPassword:" + tempUser.getLoginPassword());

			String[] info = { "", "", "" };
			String tempUrl;
			for (int j = 0; j < tempUser.getListOfUrls().size(); j++) {
				tempUrl = tempUser.getListOfUrls().get(j);

				info[0] += " " + tempUrl;
				info[0] = info[0].trim();

				info[1] += " " + tempUser.getCredentials().get(tempUrl).getUsername();
				info[1] = info[1].trim();

				info[2] += " \"" + tempUser.getCredentials().get(tempUrl).getPassword();
				info[2] = info[2].trim();
				info[2] += "\"";
			}

			writeFile("Urls:" + info[0]);
			writeFile("Usernames:" + info[1]);
			writeFile("Passwords:" + info[2]);
			writeFile("stop:" + "\n");
		}
	}

	/**
	 * This method prints out the content of the file as it is written
	 */
	public void printDataRaw() {
		try {
			scan = new Scanner(file);

			while (scan.hasNext()) {
				String line = scan.nextLine();
				System.out.println(line);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scan != null)
				scan.close();
		}
	}

	/**
	 * This is a helper method that clears the content of the file
	 */
	private void clearFile() {
		try {
			new FileWriter(file.getPath(), false).close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is a helper method that allows the file to be written in
	 * 
	 * @param input The input to write into the file
	 */
	private void writeFile(String input) {
		FileWriter fw = null;
		try {
			if (isEmpty == true) {
				fw = new FileWriter(file.getPath());
				isEmpty = false;
			} else {
				fw = new FileWriter(file.getPath(), true);
			}

			fw.write(input + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns true if the file is empty and false otherwise
	 */
	public boolean getIsFileEmpty() {
		return isEmpty;
	}
}
