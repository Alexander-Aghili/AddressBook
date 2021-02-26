import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//----------------------------------------------------------------------------------------------
//Alexander Aghili
//COMP 232
//February 8th, 2021
//----------------------------------------------------------------------------------------------
 
public class AddressBook {

	//global arraylist used to store contacts
	private static ArrayList<Contact> contactList = new ArrayList<Contact>();
	//global scanner
	private static Scanner input = new Scanner(System.in);
	private static final String COMMA_DELIMITER = ",";
	
	public static void main(String[] args) {
		downloadContacts();
		mainMenu();
	}
	
	//main menu method
	private static void mainMenu() {
		menuOptions();

		while (true) {
			boolean validCommand = false;
			System.out.print("\nSelect a command: ");
			String userInput = input.nextLine().toLowerCase().trim();
			int command = 0;
			try {
				command = Integer.parseInt(userInput);
				if (command < 0 || command > 6) {
					throw new Exception();
				}
				validCommand = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Press 6 (and enter) to see the proper list of inputs. ");
			}
			
			if (validCommand) {
				if (command == 0) {
					if(verifyUserAction("exit the program")) {
						uploadContacts();
						return;
					}
						
				}
				else if (command == 1)
					createContact();
				else if (command == 2)
					updateContact();
				else if (command == 3)
					deleteContact();
				else if (command == 4)
					sortAndListContacts();
				else if (command == 5)
					searchForContact();
				else if (command == 6)
					menuOptions();
			}
			
		}
		
	}
	
	
	//Method returns a true boolean if the user does want to continue with their action, false if they do not.
	//Asks for string parameter in order to personalize the message. 
	private static boolean verifyUserAction(String action) {
		System.out.print("\nPress enter if you want to " + action + ", otherwise, press any key and enter: ");
		String userInput = input.nextLine();
		if (userInput.trim().equals(""))
			return true;
		else
			return false;
	}
	
	//Possible menu options
	private static void menuOptions() {
		System.out.println("You may alter the Address Book in the following ways: ");
		System.out.println("Press 0 (and enter) to quit the program(Your changes are already saved).");
		System.out.println("Press 1 (and enter) to create a new contact.");
		System.out.println("Press 2 (and enter) to update a contact.");
		System.out.println("Press 3 (and enter) to delete a contact.");
		System.out.println("Press 4 (and enter) to list all of the contacts in order. (By last name, then first name, then phone number)");
		System.out.println("Press 5 (and enter) to search for a contact.");
		System.out.println("Press 6 (and enter) to see the list on inputs.");
	}
	
	//Creates contact and adds it to the arraylist and calls uploadContact to add it to csv file 
	private static void createContact() {
		System.out.println("\nEnter the information as asked, press enter by itself it exit at anytime, nothing will be saved.");
		String firstName, lastName, phoneNumber, address;
		
		System.out.print("First Name: ");
		firstName = input.nextLine().trim();
		if (firstName.equals(""))
			return;
		
		System.out.print("Last Name: ");
		lastName = input.nextLine().trim();
		if (lastName.equals(""))
			return;
		
		phoneNumber = getPhoneNumberFormatted();
		if (phoneNumber.equals("")) 
			return;
		
		System.out.print("Address: ");
		address = input.nextLine().trim();
		if (address.equals(""))
			return;
		
		Contact contact = new Contact(firstName, lastName, phoneNumber, address);
		contactList.add(contact);
		Collections.sort(contactList);
	}
	
	
	//Ensures that they input the phone number in the format asked for to make the actual formatting work
	//While true that waits for user input, goes through checks to ensure the validity of number entered.
	//breaks out of the loop if it passes, and asks for a better number if the input is bad.
	private static String getPhoneNumberFormatted() {
		String phoneNumber;
		while (true) {
			boolean validInput = true;
			System.out.print("Phone number(Enter the digits with spaces in between each section of the number): ");
			phoneNumber = input.nextLine().trim();
			if (phoneNumber.equals(""))
				return phoneNumber;
			int phoneNumLength = phoneNumber.length();
			if (phoneNumLength == 12 || phoneNumLength < 17 && phoneNumLength > 13) { 
				if (phoneNumLength == 12) {
					if (phoneNumber.charAt(3) != ' ' || phoneNumber.charAt(7) != ' ') 
						validInput = false;
				} else {
					int firstSpace = phoneNumber.indexOf(" ");
					if (phoneNumber.charAt(firstSpace + 4) != ' ' || phoneNumber.charAt(firstSpace + 8) != ' ')
						validInput = false;
				}
				for (int i = 0; i < phoneNumber.length(); i++) {	
					if (Character.isDigit(phoneNumber.charAt(i)) == false && phoneNumber.charAt(i) != ' ')
						validInput = false;
				}
			}
			else 
				validInput = false;
			
			if (validInput == true) 
				return phoneNumberFormatter(phoneNumber);
			else
				System.out.println("This is an incorrect format or number, ensure you entered the number in correctly and as described. \n");
		}
	}
	
	//Formats a number properly spaced, which is done through the getPhoneNumberFormatted method
	private static String phoneNumberFormatter(String phoneNumber) {
		if (phoneNumber.length() > 12) {
			int firstSpace = phoneNumber.indexOf(" ");
			return "+" + phoneNumber.substring(0, firstSpace) + " (" 
			+ phoneNumber.substring(firstSpace+1, firstSpace+4) + ") " + phoneNumber.substring(firstSpace+5, firstSpace+8)
			+ "-" + phoneNumber.substring(firstSpace+9, firstSpace + 13);
		} else {
			return "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(4, 7) + "-" + phoneNumber.substring(8);
 		}
	}
	
	
	//Update contact main menu, calls getPartOfContactUpdate in order to get which part of the contact
	//the  user wants to update
	private static void updateContact() {
		if (contactList.size() == 0) {
			System.out.println("You have no contacts, press 1 to add some!");
			return;
		}
		
		for (int i = 0; i < contactList.size(); i++) {
			System.out.println((i+1) + "." + contactList.get(i) + "\n");
		}
		
		while(true) {
			System.out.print("Which contact from the list would you like to update? (Select the number, enter nothing to exit): ");
			String userInput = input.nextLine().toLowerCase().trim();
			if (userInput.equals("")) {
				Collections.sort(contactList);
				return;
			}
			try {
				int contactNum = Integer.parseInt(userInput);
				
				if(contactNum < 1 || contactNum > contactList.size())
					throw new Exception();
				int changeContactPart = getPartOfContactUpdate();
				
				if (changeContactPart == 0)
					return;
				else if (changeContactPart == 1) {
					System.out.print("First Name: ");
					contactList.get(contactNum-1).setFirstName(input.nextLine().trim());
				}
				else if (changeContactPart == 2) {
					System.out.print("Last Name: ");
					contactList.get(contactNum-1).setLastName(input.nextLine().trim());
				}
				else if (changeContactPart == 3) {
					contactList.get(contactNum-1).setPhoneNumber(getPhoneNumberFormatted());
				}
				else if (changeContactPart == 4) {
					System.out.print("Address: ");
					contactList.get(contactNum-1).setAddress(input.nextLine().trim());
				}
				System.out.println("Contact successfully updated.\n");
				
			} catch (Exception e) {
				System.out.println("Bad input, try again.\n");
			}
		}
		
	}
	
	//Gets which part of the contact the user wants to update, uses traditional menu structure
	//returns a value from 1 to 4 unless it is "", in which case it returns a 0, conditional
	//is used in contactUpdate to ensure that it returns if it is 0.
	private static int getPartOfContactUpdate() {
		System.out.println("\nPress enter to exit. ");
		System.out.println("Press 1 to change the first name. ");
		System.out.println("Press 2 to change the last name. ");
		System.out.println("Press 3 to change the phone number. ");
		System.out.println("Press 4 to change the address. \n");
		
		while (true) {
			
			System.out.print("Enter the number for what you would like to update: ");
			String userInput = input.nextLine().trim();
			if (userInput.equals(""))
				return 0;
			try {
				int getContactUpdateNum = Integer.parseInt(userInput);
				if (getContactUpdateNum < 1 || getContactUpdateNum > 4)
					throw new Exception();
				return getContactUpdateNum;
			} catch (Exception e) {
				System.out.print("Enter a number from 1 to 4(or enter to exit)");
			}
		}
	}
	
	//Deletes contact using basic menu structure.
	private static void deleteContact() {
		if (contactList.size() == 0) {
			System.out.println("You have no contacts, press 1 to add some!");
			return;
		}
		
		for (int i = 0; i < contactList.size(); i++) {
			System.out.println((i+1) + "." + contactList.get(i) + "\n");
		}
		
		while(true) {
			if (contactList.size() == 0) {
				System.out.println("You have no more contacts.");
				return;
			}
			
			System.out.print("Which contact from the list would you like to delete? (Select the number, enter nothing to exit): ");
			String userInput = input.nextLine().toLowerCase().trim();
			
			if (userInput.equals(""))
				return;
			
			try {
				int contactNum = Integer.parseInt(userInput);
				
				if(contactNum < 1 || contactNum > contactList.size())
					throw new Exception();
				
				if (verifyUserAction("delete contact number " + contactNum)) {
					contactList.remove(contactNum - 1);
					System.out.println("Contact successfully deleted, return to main menu...");
					return;
				}
				
			} catch (Exception e) {
				System.out.println("Bad input, try again. \n");
			}
		}
		
	}
	
	private static void sortAndListContacts() {
		if (contactList.size() == 0) {
			System.out.println("You have no contacts, press 1 to add some!");
			return;
		}
		
		Collections.sort(contactList);
		System.out.println("\nThe following are you contacts in order: ");
		for (int i = 0; i < contactList.size(); i++) {
			System.out.println((i+1) + "." + contactList.get(i) + "\n");
		}
	}
	
	private static void searchForContact() {
		if (contactList.size() == 0) {
			System.out.println("You have no contacts, press 1 to add some!");
			return;
		}
		
		System.out.println("\nYou can search by...");
		System.out.println("1. First Name");
		System.out.println("2. Last Name");
		System.out.println("3. Phone Number");
		System.out.println("Press enter to return to main menu.\n");
		
		while(true) {
			System.out.print("Select the number of how you would like to search: ");
			String userInput = input.nextLine().trim();
			if (userInput.equals(""))
				return;
			
			try {
				int getSearchChoice = Integer.parseInt(userInput);
				if (getSearchChoice < 1 || getSearchChoice > 3)
					throw new Exception();
				
				if (getSearchChoice == 1)
					searchByFirstName();
				else if (getSearchChoice == 2)
					searchByLastName();
				else if (getSearchChoice == 3)
					searchByPhoneNumber();
					
				System.out.println("\nReturning to main menu...");
				return;
			} catch (Exception e) {
				System.out.println("Bad input, try again. \n");
			}
		}
	}
	
	
	//Searching by linear short in O(n) time, searches for first name match
	private static void searchByFirstName() {
		System.out.print("Search by a first name: ");
		String searchName = input.nextLine().trim();
		if (searchName.equals(""))
			return;
		
		for (int i = 0; i < contactList.size(); i++) {
			
			
			if (searchName.compareTo(contactList.get(i).getFirstName()) == 0) {
				System.out.println("Contact number " + (i + 1) 
						+ " has the phone number " + searchName + " and details are listed below.");
				System.out.println(contactList.get(i));
				return;
			}
		}
		
		System.out.println("No contact contains the first name " + searchName);
		
		
	}

	//Searching by linear short in O(n) time, searches for last name match
	//Wasn't able to properly implement binary search due to time, can be implemented at a later date
	private static void searchByLastName() {
		System.out.print("Search by a last name: ");
		String searchName = input.nextLine().trim();
		if (searchName.equals(""))
			return;
		
		for (int i = 0; i < contactList.size(); i++) {
			if (searchName.compareTo(contactList.get(i).getLastName()) == 0) {
				System.out.println("Contact number " + (i + 1) 
						+ " has the phone number " + searchName + " and details are listed below.");
				System.out.println(contactList.get(i));
				return;
			}
		}
		
		System.out.println("No contact contains the first name " + searchName);
	}

	//Searching by linear short in O(n) time, searches for phone number match
	private static void searchByPhoneNumber() {
		System.out.print("Search by a phone number: ");
		String searchNumber = getPhoneNumberFormatted();
		if (searchNumber.equals(""))
			return;
		
		for (int i = 0; i < contactList.size(); i++) {
			
			
			if (searchNumber.compareTo(contactList.get(i).getPhoneNumber()) == 0) {
				System.out.println("Contact number " + (i + 1) 
						+ " has the phone number " + searchNumber + " and details are listed below.");
				System.out.println(contactList.get(i));
				return;
			}
		}
		
		System.out.println("No contact contains the phone number " + searchNumber);
	}

	
	//uploads contacts to Contacts.csv file, readds all contacts in order to make sure all updates and new 
	//contacts are properly added.
	private static void uploadContacts() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("Contacts.csv"));
			bw.write("FirstName,LastName,PhoneNumber,Address");
			for (Contact contact: contactList) {
				bw.newLine();
				String contactInformation = contact.getFirstName() + "," + contact.getLastName() + "," 
											+ contact.getPhoneNumber() + "," + contact.getAddress();
				bw.write(contactInformation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { bw.close(); }
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return;
	}
	
	
	//Run at the beginning to get all the contacts from Cotnacts.csv and adds it to the contactList array list
	private static void downloadContacts() {
		BufferedReader br = null;
		try { 
			File contactFile = new File("Contacts.csv");
			contactFile.createNewFile();
			br = new BufferedReader(new FileReader("Contacts.csv"));
			String line = "";
			br.readLine();
			while((line = br.readLine()) != null) {
				String[] contactInformation = line.split(COMMA_DELIMITER);
				Contact contact = new Contact(contactInformation[0], contactInformation[1], 
						contactInformation[2], contactInformation[3]);
				
				contactList.add(contact);
			}
			
			//Sort arraylist
			Collections.sort(contactList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { br.close(); } 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
