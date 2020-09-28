// --== CS400 File Header Information ==--
// Name: Aneesh Patil
// Email: apatil6@wisc.edu
// Team: GA
// TA: Daniel Kiel
// Lecturer: Gary Dahl
// Notes to Grader: NA

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This class models the representation of a generic Password Manager including
 * the interface. User will be prompted to input login details or register
 * 
 * @author barna, aneesh
 *
 */
public class PasswordManager {

	private FileUtility utility;
	private Scanner scan;
	private HashTableMap<String, User> users;
	private LinkedList<String> listOfUsernames;
	private boolean isRunning = true;

	public PasswordManager() {
		utility = new FileUtility(new File("Data.txt"));
		users = new HashTableMap<>();
		listOfUsernames = new LinkedList<>();
		utility.loadData(users, listOfUsernames);
		scan = new Scanner(System.in);
	}

	/**
	 * This method is the userInterface where the user can interact with the program
	 * 
	 * @author barna, aneesh
	 */
	public void userInterface() {
		String tempName, tempPass, tempUrl;
		User tempUser = null;
		Data tempData = null;
		boolean loginSuccess = false;
		System.out.println("**********************************************************************");
		System.out.println("");
		System.out.println("*************   WELCOME TO PASSWORD MANAGER APPLICATION   ************");
		System.out.println("");
		System.out.println("**********************************************************************");
		System.out.println("");

		// Login interface to login into the password manager
		while (isRunning) {

			boolean loginOrRegister = true;

			while (loginOrRegister) {
				System.out.print("If you're an existing member, press 'a'. If you're new, press 'b' to register: ");

				String tempResponse = scan.nextLine();

				if (tempResponse.equals("b")) {
					addNewUserHelper();
					loginOrRegister = false;
					break;
				} else if (!tempResponse.equals("a") && !tempResponse.equals("b")) {
					System.out.println("Please enter a valid input");
					System.out.println("");
				} else {
					// loginOrRegister = false;
					break;
				}
			}
			System.out.println("");
			System.out.print("To login, enter your username: ");
			tempName = scan.nextLine();

			if (users.containsKey(tempName)) {
				tempUser = (User) users.get(tempName);

				boolean incorrectPass = true;

				int count = 0;

				while (incorrectPass) {
					System.out.print("Enter password: ");
					tempPass = scan.nextLine();
					if (tempUser.getLoginPassword().equals(tempPass)) {
						loginSuccess = true;
						incorrectPass = false;
					} else {
						count++;
						if (count == 3) {
							System.out.println("Too many incorrect attempts. Please wait for a few moments to retry.");
							try {
								Thread.sleep(60000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							count = 0;
						}
						System.out.println("");
						System.out.println("Incorrect password");
						System.out.print("Enter any key to retry or 'q' to exit: ");
						String input = scan.nextLine();
						System.out.println("");

						if (input.trim().equals("q")) {
							isRunning = false;
							break;
						}
					}
				}

			} else {
				System.out.println("No such username exists");
				System.out.println("");
				continue;
			}

			// User Interface where user is in the actual password manager
			if (loginSuccess == true) {

				String realName = tempName;

				boolean isRunning = true;

				System.out.println("Authentication Successful. Logging you in...");

				while (isRunning) {
					System.out.println("");
					System.out.println("**********************************************************************");
					System.out.println("Welcome Back " + realName + "!");
					System.out.println("");
					System.out.println("Which of the following would you like to perform? ");
					System.out.println("Search a new URL: press 'y'");
					System.out.println("Add a new URL: press 'a'");
					System.out.println("Update account password: press 'u'");
					System.out.println("Change user: press 'c'");
					System.out.println("Add new user: press 'b'");
					System.out.println("Quit the app: press 'q'");
					System.out.println("");
					System.out.print("Input: ");

					// System.out.print(
					// "Press y(search new Url), q(exit app), c(change user), a(add new url), b(add
					// new user), u(update password)");

					String input = scan.nextLine().toLowerCase();
					System.out.println("");

					if (input.equals("y")) {

						boolean retry = true;

						System.out.println("What URL's credentials would you like to retrieve? ");

						while (retry) {

							System.out.print("URL: ");
							tempUrl = scan.nextLine().trim().toLowerCase();
							if (tempUser.getCredentials().containsKey(tempUrl)) {
								tempData = (Data) tempUser.getCredentials().get(tempUrl);
								tempName = tempData.getUsername();
								tempPass = tempData.getPassword();

								System.out.println("");
								System.out.println("Retrieving username and password...");
								System.out.println("");
								System.out.println("Username for " + tempUrl + ": " + tempName);
								System.out.println("Password for " + tempUrl + ": " + tempPass + "\n");

								System.out.print("Enter any key to return to main menu: ");
								scan.nextLine();

								/*
								 * try { Thread.sleep(3000); } catch (InterruptedException e) {
								 * e.printStackTrace(); }
								 */
								retry = false;

							}

							else {
								System.out.println("");
								System.out.println("Account does not exist for this URL. Please try again");
							}
						}

					} else if (input.equals("q")) {

						this.isRunning = false;
						isRunning = false;
						System.out.println("**********************************************************************");
						System.out.println("");
						System.out.println("*************    Thank you for using Password Manager    ************");
						System.out.println("");
						System.out.println("**********************************************************************");
						System.out.println("");
						break;
					} else if (input.equals("c")) {
						isRunning = false;
						break;
					} else if (input.equals("a")) {

						addNewURLHelper(realName);
						break;
					} else if (input.equals("b")) {

						addNewUserHelper();
						break;
					} else if (input.equals("u")) {
						System.out.print("To update password please enter your username: ");
						String userName = scan.nextLine().trim();
						System.out.print(
								"Enter password of at least 6 characters including letters, numbers and ! or ?: ");
						String userPass = scan.nextLine().trim();
						updatePassword(userName, userPass);
						break;
					} else {
						System.out.println("Url does not exist");
					}
				}
			}
		}

		System.out.println("Exiting and saving application");
		utility.saveData(users, listOfUsernames);// Dont forget this as this will save the changes
	}

	/**
	 * This method adds new user with a new username and password String into the
	 * users hashTable. duplicates of usernames are not allowed
	 * 
	 * @param username The String that contains a chosen username
	 * @param password The String that contains a chosen password
	 */
	public void addNewUser(String username, String password) {
		for (int i = 0; i < listOfUsernames.size(); i++) {
			if (username.equals(listOfUsernames.get(i))) {
				System.out.println("Username already taken.");
				return;
			}
		}

		users.put(username, new User(username, password));
		listOfUsernames.add(username);

	}

	/**
	 * This helper method aids in adding a new user with a strong password
	 * 
	 * @author Jeff, aneesh
	 */
	public void addNewUserHelper() {
		System.out.println("");
		System.out.print("Enter new username: ");
		String user = scan.nextLine().trim();

		while (listOfUsernames.contains(user)) {
			System.out.println("Username already exists. Try again:");

			System.out.print("Enter new username: ");
			user = scan.nextLine().trim();
		}

		System.out.print("Enter password of at least 6 characters including letters, numbers and ! or ?: ");
		String pass = scan.nextLine().trim();
		while (!validatePassword(pass)) {
			System.out.println("Password not secure.");
			System.out.println("");
			System.out.print("Enter password of at least 6 characters including letters, numbers and ! or ?: ");
			pass = scan.nextLine().trim();
		}

		addNewUser(user, pass);
		System.out.println("Successfuly added new user.");
		System.out.println("");

	}

	/**
	 * This helper method aids in adding a new url with a username password combo
	 * 
	 * @author Jeff
	 */
	public void addNewURLHelper(String username) {
		String usernameInp = username;
		String urlInp;
		String urlPass;
		String urlUser;

		while (!users.containsKey(usernameInp)) {
			System.out.println("Username doesn't exist\n");
			System.out.print("Enter your username: ");
			usernameInp = scan.nextLine().trim();
		}

		System.out.print("Enter new URL: ");

		urlInp = scan.nextLine().trim();

		System.out.print("Username for URL: ");
		urlUser = scan.nextLine().trim();

		System.out.print("Password for URL: ");
		urlPass = scan.nextLine().trim();

		System.out.println("");
		System.out.println("New URL with Username and Password added.");

		addNewCredential(usernameInp, urlInp, urlUser, urlPass);
		this.isRunning = false;
		isRunning = false;

	}

	/**
	 * This method adds a chosen Url, username and a password and associate it with
	 * a User object
	 * 
	 * @param loginUsername The String of the initial login username to associate
	 *                      the url, username, and password to
	 * @param url           The String that contains a chosen url
	 * @param username      The String that contains a chosen username
	 * @param password      The String that contains a chosen password
	 */
	public void addNewCredential(String loginUsername, String url, String username, String password) {
		((User) users.get(loginUsername)).addCredential(url, username, password);

	}

	/**
	 * This method checks if a username exists
	 * 
	 * @author Jeff
	 * @return true if it exists
	 */
	public boolean userNameExist(String username) {
		return (users.containsKey(username));
	}

	/**
	 * This method updates a existing user's password with a new password
	 * 
	 * @author Jeff
	 * @param username-    existing username
	 * @param newPassword- new password being added
	 */
	public void updatePassword(String username, String newPassword) {
		while (!validatePassword(newPassword)) {
			System.out.println("Password not Secure\n");
			System.out.print("Enter password of at least 6 characters including letters, numbers and ! or ?: ");
			newPassword = scan.nextLine().trim();
		}
		User tempUser = null;

		if (users.containsKey(username)) {
			tempUser = (User) users.get(username);
		}
		tempUser.setLoginPassword(newPassword);

		System.out.println("Password Updated Successfully\n");
	}

	/**
	 * This helper method checks to see if password matches the criteria of having
	 * length 6 with numbers and letters and either a ! or ? symbol.
	 * 
	 * @author Jeff
	 * @param pass - password input
	 */
	private static boolean validatePassword(String pass) {
		boolean checkLength = false;
		if (pass.length() >= 6)
			checkLength = true;

		boolean containLetters = false;
		for (int i = 0; i < pass.length(); i++) {
			if (Character.isLetter(pass.charAt(i))) {
				containLetters = true;
				break;
			}
		}

		boolean containDigits = false;
		for (int i = 0; i < pass.length(); i++) {
			if (Character.isDigit(pass.charAt(i))) {
				containDigits = true;
				break;
			}
		}

		boolean containSymbol = false;
		for (int i = 0; i < pass.length(); i++) {
			if (pass.charAt(i) == '!' || pass.charAt(i) == '?') {
				containSymbol = true;
				break;
			}
		}
		return (checkLength && containLetters && containDigits && containSymbol);
	}

	/**
	 * Main method that runs the program.
	 * 
	 */
	public static void main(String[] args) {
		PasswordManager pm = new PasswordManager();
		pm.userInterface();
	}

}