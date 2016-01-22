import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Collections;

public class Contact_List {
	/*
		my_csv stores the location of the csv file being read and written on.

		the_contacts stores all of the contacts read from the csv file and added
		by the user.

		duplicate_exists is a boolean to be used for the check_for_duplicates function.
	*/
	private static CSV_readwrite my_csv;
	private ArrayList<Contact> the_contacts;
	private Boolean duplicate_exists = false;

	/*
		The following quicksort implementation is based on the Hoare
		partition scheme as seen on Wikipedia.
	*/
	public void quicksort(ArrayList<Contact> A, int lo, int hi) {
		/*
			"lo" in this case is the lowest index of the contact
			list, and "hi" is the max index.

			If lo < hi, create a middle part within the list via
			"pivotindex."
		*/
		Contact loContact = A.get(lo);
		Contact hiContact = A.get(hi);

		if (lo < hi) {
			// int pivotIndex = (lo + hi) / 2;

			/*
				Create a partition point that splits the Contact list,
				so that quick sort can be recursively performed on
				both halves.
			*/
			int p = partition(A, lo, hi);
			quicksort(A, lo, p);
			quicksort(A, p + 1, hi);
		}
	}

	public int partition(ArrayList<Contact> A, int lo, int hi) {
		Contact pivot = A.get(lo);
		
		int i = lo - 1;
		int j = hi + 1;
		
		/*
			The "pivot" contact is set by the "lo" index, so in this
			case 0.

			For the sake of explanation, the Contact based on the right
			index will be called R, the Contact based on the left
			index will be called L, and the Contact based on the pivot
			index will be called P.

			Increment the left index by 1 while L's ID is less than P's ID,
			Decrement the right index by 1 while R's ID is greater than P's ID, 
			
			Note that the Do-While means the condition is not considered until after
			the statements are run, meaning the statements will run at least once.
	
			Eventually the left and right index end up as close to the pivot
			point as possible.

			At that point, if the left index is less than the right index, and
			L's ID does not equal R's ID, swap L with R.

			Otherwise, the result is a partition point, because either i > j or
			L == R, meaning there is a definitive
			partition point, so between the two options index j is returned.
		*/


		while (true) {
			do {
				j = j - 1;
			} while (A.get(j).get_ID() > pivot.get_ID());
			do {
				i = i + 1;
			} while (A.get(i).get_ID() < pivot.get_ID());
			
			if (i < j && A.get(i).get_ID() != 
				A.get(j).get_ID()) {

				Collections.swap(A, i, j);
			}
			
			else {
				return j;
			}
		}
	}


	// Set the_contacts, the ArrayList within the class, as an empty Contact ArrayList.
	public Contact_List() {

		the_contacts = new ArrayList<Contact>();
	}
	
	/*
		add_contact adds a contact to the contact list based on direct input, while
		add_new_contact takes a contact as input to add.
	*/
	public void add_contact() {
		int id;
		String zip;
		String first_name;
		String last_name;
		String address;
		String city;
		String state;
		String phone_num;

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			/*
				Ask first for an id to add, then check if it already exists in the contacts list.

				If it does, then print a message showing which contacts use the ID.
			*/
			System.out.println("Add ID: ");
			id = Integer.parseInt(reader.readLine());

			ArrayList<Contact> test_contacts = check_for_id(id);

			while (test_contacts.size() > 0) {
				System.out.println("Sorry, that ID is already being used for the following " + test_contacts.size() + " contacts:");
				print_list(test_contacts);
				System.out.println("\n" + "Please try another ID:");
				id = Integer.parseInt(reader.readLine());
				test_contacts = new ArrayList<Contact>();
				test_contacts = check_for_id(id);
			}
			
			/*
				Retrieve all other components of the contact record to add, create the
				contact and add it to the csv file.
			*/
			System.out.println("Add First Name: ");
			first_name = reader.readLine();
			System.out.println("Add Last Name: ");
			last_name = reader.readLine();
			System.out.println("Add Address: ");
			address = reader.readLine();
			System.out.println("Add City/Town: ");
			city = reader.readLine();
			System.out.println("Add State (Initials): ");
			state = reader.readLine();
			System.out.println("Add Zip Code: ");
			zip = reader.readLine();
			System.out.println("Add Phone Number: ");
			phone_num = reader.readLine();

			Contact new_contact = new Contact(	id,
									first_name,
									last_name,
									address,
									city,
									state,
									zip,
									phone_num);

			the_contacts.add(new_contact);
			my_csv.one_csv_write(new_contact);
		} catch (Exception e) {
			System.out.println("Error!");
			e.printStackTrace();
		}	
	}
	
	public void add_new_contact (Contact c, CSV_readwrite my_csv) {
		the_contacts.add(c);
		String filename = System.getProperty("user.home")+"/contact_list.csv";
		my_csv.one_csv_write(c);
	}

	/*
		check_for_duplicates creates a HashMap for storing all IDs as keys,
		and the amount of times it appears in the records as the value.

		Run through the contacts list, temporarily store the given ID as
		int key. If the HashMap contains the key already, print out a
		notification, and increment the value associated with that key in the
		HashMap by 1.

		Otherwise, the value is equal to 1 because the key has yet to be stored.
	*/
	public HashMap<Integer, Integer> check_for_duplicates() {
		HashMap<Integer, Integer> duplicate_map = new HashMap<Integer, Integer>();
		for (Contact c: the_contacts) {
			int key = c.get_ID();
			if (duplicate_map.containsKey(key)) {
				System.out.println("DUPLICATE FOR ID: " + key);
				String record_one_line = c.print_one_line();
				System.out.println("RECORD: " + record_one_line);
				duplicate_map.put(key, duplicate_map.get(key) + 1);
				duplicate_exists = true;
			}
			else {
				duplicate_map.put(key, 1);	
			}
			
		}

		for (int name: duplicate_map.keySet()){
			int key = name; 
			int value = duplicate_map.get(name);
			if (value > 1) {
				System.out.println("\n\nDUPLICATES FOR ID: " + key);
				System.out.println(key + ": " + value);
			}
		}
		return duplicate_map;
	}

	/*
		For all of the following check functions, create an empty ArrayList based on
		the component in question (id, first name, etc.). Loop through the_contacts
		list, and for every time there is a match, add that to the empty ArrayList.

		Return the ArrayList at the end.
	*/
	public ArrayList<Contact> check_for_id(int id) {
		ArrayList<Contact> id_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			if (id == c.get_ID()) {
				id_check.add(c);
			}
		}
		return id_check;
	}
	
	public ArrayList<Contact> check_for_first_name(String first_name) {
		ArrayList<Contact> first_name_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_first_name(), first_name);
			
			if (diff.equals("")) {
				first_name_check.add(c);
			}
			
		}
		return first_name_check;
	}

	public ArrayList<Contact> check_for_last_name(String last_name) {
		ArrayList<Contact> last_name_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_last_name(), last_name);
			if (diff.equals("")) {
				last_name_check.add(c);
			}
		}
		return last_name_check;
	}

	public ArrayList<Contact> check_for_address(String address) {
		ArrayList<Contact> address_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_address(), address);
			if (diff.equals("")) {
				address_check.add(c);
			}
		}
		return address_check;
	}

	public ArrayList<Contact> check_for_city(String city) {
		ArrayList<Contact> city_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_city(), city);
			if (diff.equals("")) {
				city_check.add(c);
			}
		}
		return city_check;
	}

	public ArrayList<Contact> check_for_state(String state) {
		ArrayList<Contact> state_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_state(), state);
			if (diff.equals("")) {
				state_check.add(c);
			}
		}
		return state_check;
	}

	public ArrayList<Contact> check_for_zip(String zip) {
		ArrayList<Contact> zip_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_zip(), zip);
			if (diff.equals("")) {
				zip_check.add(c);
			}
		}
		return zip_check;
	}

	public ArrayList<Contact> check_for_phone(String phone) {
		ArrayList<Contact> phone_check = new ArrayList<Contact>();
		for (Contact c: the_contacts) {
			String diff = difference(c.get_phone(), phone);
			if (diff.equals("")) {
				phone_check.add(c);
			}
		}
		return phone_check;
	}

	/*
		Run through the_contacts, and check for a contact based on id.

		If it is found, remove the contact and print out a confirmation.

		A boolean is included to add an option to show there is nothing
		to be deleted.
	*/
	public void delete_contact(int id_to_delete) {
		Boolean check = false;
		for (Contact c: the_contacts) {
			if (c.get_ID() == id_to_delete) {
				the_contacts.remove(c);
				check = true;
				System.out.println("Item Deleted for ID: " + c.get_ID());
				System.out.println("There are now " + the_contacts.size() + " records left.");
				break;
			}
		}
		if (!check) {
			System.out.println("ID not found! Nothing to delete!");
		}
	}

	/*
		Function for finding the point of difference between two strings.

		Loop through both strings by character simultaneously, and stop at the
		moment the characters from each string do not equal each other at the
		same index.
	*/
	public static String difference(String s1, String s2) {
		for (int i = 0; i < s1.length() && i < s2.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				
				if (Character.toString(s2.charAt(i)).equals(null))
					return Character.toString(s1.charAt(i));
				
				else if (Character.toString(s1.charAt(i)).equals(null))
					return Character.toString(s2.charAt(i));
				
				return Character.toString(s1.charAt(i));
			}
		}
		return "";
	}

	/*
		edit_contact is the function used for picking the method for searching
		and editing a contact.

		First options are printed out via edit_options, and a choice is read in based
		on said options.

		A loop is created so that the user can edit one user after another by whichever
		options he/she chooses, until the user decides to exit the edit contact menu, via
		choice 9.
	*/
	public void edit_contact() {
		edit_options();
		
		int choice = -1;
		int delete_id = -1;

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			choice = Integer.parseInt(reader.readLine());	
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		while (choice != 9) {
			if (choice == 1) {
				edit_by_ID();
			}
			else if (choice == 2) {
				edit_by_FirstName();
			}
			else if (choice == 3) {
				edit_by_LastName();
			}
			else if (choice == 4) {
				edit_by_Address();
			}
			else if (choice == 5) {
				edit_by_City();
			}
			else if (choice == 6) {
				edit_by_State();
			}
			else if (choice == 7) {
				edit_by_ZIP();
			}
			else if (choice == 8) {
				edit_by_Phone();
			}
			else if (choice != 9) {
				System.out.println("Invalid choice.");
			}
			edit_options();
			try {
				choice = Integer.parseInt(reader.readLine());	
			} catch (Exception e) {
				System.out.println("Error AGAIN!!!");
				e.printStackTrace();
			}
		}
	}
		
	public void edit_options() {
		System.out.println("EDIT CONTACT:");
		System.out.println("Please select choice/method to search contact:");
		System.out.println("1: ID");
		System.out.println("2: First Name");
		System.out.println("3: Last Name");
		System.out.println("4: Address");
		System.out.println("5: City");
		System.out.println("6: State");
		System.out.println("7: Zip");
		System.out.println("8: Phone Number");
		System.out.println("9: Exit Edit");
	}
	
	/*
		The following are edit function designed for each component of
		the contact record.

		For each function, first input the component in question to search
		for the contact to edit. That component is then sent to the appropriate
		check_for function seen above.

		If the ArrayList returned from the check function has a size of 0, that means
		the id does not exists in the current records, and the user is asked to
		try again until the ArrayList's size is > 0. 

		If that ArrayList is > 1, all options for contacts to edit are shown to pick
		from, otherwise the one contact is shown. THe user will then add the new component
		(ID, first name, etc.), which is then again double checked to see if it exists
		in the contact records.
	*/
	public void edit_by_ID() {
		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Contact contactEdit = null;
		int id_original = -1;
		int id_new = -1;
		int id_duplicate = -1;
		

		System.out.println("Add ID: ");
		
		try {
			id_original = Integer.parseInt(reader.readLine());
		} catch (Exception e) {
			System.out.println("Error Reading ID!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_id(id_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that ID does not exist in the records!");
			System.out.println("\n" + "Please try another ID:");
			try {
				id_original = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_id(id_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original id.\n");
			System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose ID based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
		}

		System.out.println("Here is your chosen contact:");
		System.out.println(id_duplicate + " : " + contactEdit.simplePrint());

		System.out.println("\n" + "Please add new ID:");
		
		try {
			id_new = Integer.parseInt(reader.readLine());
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}
		
		test_contacts = check_for_id(id_new);

		while (test_contacts.size() >= 1) {
			System.out.println("\n\nSorry! There is more than one contact for this id.\n");
			System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
			print_list(test_contacts);
			System.out.println("\n\nPlease try another id.");

			try {
				id_new = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			test_contacts = check_for_id(id_new);

		}
		

		contactEdit.set_ID(id_new);

		System.out.println("ID changed! Here is the result:");

		print_one_contact(contactEdit);
	}
	
	public void edit_by_FirstName(){
		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String first_name_original = new String();
		String first_name_new = new String();
		String duplicate_id_string = new String();
		int duplicate_id = -1;
		

		System.out.println("Add first name: ");
		
		try {
			first_name_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading first name!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_first_name(first_name_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that first name does not exist in the records!");
			System.out.println("\n" + "Please try another first name:");
			try {
				first_name_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_first_name(first_name_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original first name.");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose ID based on above list to select duplicate contact to edit:");
			
			int maxID = contact_list_map.size();

			try {
				
				duplicate_id = Integer.parseInt(duplicate_id_string);
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			
			System.out.println("DUPLICATE ID, MAXID: " + duplicate_id + " " + maxID);

			while (duplicate_id < 1 || duplicate_id > maxID) {
				if (duplicate_id < 1 || duplicate_id > maxID){
					System.out.println("Sorry, that ID does not exist in the above list!");
					System.out.println("\n" + "Please try again:");
					try {
						duplicate_id_string = reader.readLine();
						duplicate_id = Integer.parseInt(duplicate_id_string);
					} catch (Exception e) {
						System.out.println("Error Reading Choice!");
						e.printStackTrace();
					}
				}
			}

			contactEdit = contact_list_map.get(duplicate_id);
			

			System.out.println("Here is your chosen contact:");
			System.out.println(duplicate_id + " : " + contactEdit.simplePrint());
		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
			System.out.println("Here is your chosen contact:");
			System.out.println(contactEdit.simplePrint());
		}

		

		System.out.println("\n" + "Please add new first name:");

		try {
			first_name_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		contactEdit.set_first_name(first_name_new);

		System.out.println("First name changed! Here is the result:");

		print_one_contact(contactEdit);
	}

	public void edit_by_LastName(){
		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String last_name_original = new String();
		String last_name_new = new String();
		String last_name_duplicate = new String();
		int id_duplicate = -1;
		

		System.out.println("Add last name: ");
		
		try {
			last_name_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading last name!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_last_name(last_name_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that last name does not exist in the records!");
			System.out.println("\n" + "Please try another last name:");
			try {
				last_name_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_last_name(last_name_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original last name.");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose last name based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

			System.out.println("Here is your chosen contact:");
			System.out.println(last_name_duplicate + " : " + contactEdit.simplePrint());
		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
			System.out.println("Here is your chosen contact:");
			System.out.println(contactEdit.simplePrint());
		}

		

		System.out.println("\n" + "Please add new last name:");

		try {
			last_name_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		contactEdit.set_last_name(last_name_new);

		System.out.println("last name changed! Here is the result:");

		print_one_contact(contactEdit);
	}

	public void edit_by_Address(){

		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String address_original = new String();
		String address_new = new String();
		int id_duplicate = -1;
		

		System.out.println("Add address: ");
		
		try {
			address_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading address!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_address(address_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that address does not exist in the records!");
			System.out.println("\n" + "Please try another address:");
			try {
				address_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_address(address_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original address.");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose address based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

			System.out.println("Here is your chosen contact:");
			System.out.println(id_duplicate + " : " + contactEdit.simplePrint());
		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
			System.out.println("Here is your chosen contact:");
			System.out.println(contactEdit.simplePrint());
		}

		

		System.out.println("\n" + "Please add new address:");

		try {
			address_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		test_contacts = check_for_address(address_new);

		while (test_contacts.size() >= 1) {
			System.out.println("\n\nSorry! There is more than one contact for this address.\n");
			System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
			print_list(test_contacts);
			System.out.println("\n\nPlease try another id.");

			try {
				address_new = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			test_contacts = check_for_address(address_new);

		}

		contactEdit.set_address(address_new);

		System.out.println("address changed! Here is the result:");

		print_one_contact(contactEdit);		
	}

	public void edit_by_City(){

		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String city_original = new String();
		String city_new = new String();
		int id_duplicate = -1;
		

		System.out.println("Add city: ");
		
		try {
			city_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading city!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_city(city_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that city does not exist in the records!");
			System.out.println("\n" + "Please try another city:");
			try {
				city_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_city(city_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original city.");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose city based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

			System.out.println("Here is your chosen contact:");
			System.out.println(id_duplicate + " : " + contactEdit.simplePrint());
		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
			System.out.println("Here is your chosen contact:");
			System.out.println(contactEdit.simplePrint());
		}

		

		System.out.println("\n" + "Please add new city:");

		try {
			city_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		test_contacts = check_for_city(city_new);

		contactEdit.set_city(city_new);

		System.out.println("city changed! Here is the result:");

		print_one_contact(contactEdit);		
	}

	public void edit_by_State(){

		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String state_original = new String();
		String state_new = new String();
		String state_duplicate = new String();
		int id_duplicate = -1;
		

		System.out.println("Add State (Initials): ");
		
		try {
			state_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading state!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_state(state_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that state does not exist in the records!");
			System.out.println("\n" + "Please try another state:");
			try {
				state_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_state(state_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original state.");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose state based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

			System.out.println("Here is your chosen contact:");
			System.out.println(id_duplicate + " : " + contactEdit.simplePrint());
		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
			System.out.println("Here is your chosen contact:");
			System.out.println(contactEdit.simplePrint());
		}

		

		System.out.println("\n" + "Please add new state:");

		try {
			state_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		contactEdit.set_state(state_new);

		System.out.println("State changed! Here is the result:");

		print_one_contact(contactEdit);	
	}

	public void edit_by_ZIP(){

		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Contact contactEdit = null;
		String zip_original = new String();
		String zip_new = new String();
		int id_duplicate = -1;
		

		System.out.println("Add zip: ");
		
		try {
			zip_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading zip!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_zip(zip_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that zip does not exist in the records!");
			System.out.println("\n" + "Please try another zip:");
			try {
				zip_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_zip(zip_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original zip.");
			print_list(test_contacts);


			System.out.println("\nSeparate zip will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose zip based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
		}

		System.out.println("Here is your chosen contact:");
		System.out.println(id_duplicate + " : " + contactEdit.simplePrint());

		System.out.println("\n" + "Please add new zip:");

		try {
			zip_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		contactEdit.set_zip(zip_new);

		System.out.println("zip changed! Here is the result:");

		print_one_contact(contactEdit);
	}

	public void edit_by_Phone(){

		HashMap<Integer, Contact> contact_list_map = new HashMap<Integer, Contact>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String phone_original = new String();
		String phone_new = new String();
		int id_duplicate = -1;

		System.out.println("Add phone: ");
		
		try {
			phone_original = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading phone!");
			e.printStackTrace();
		}
		
		ArrayList<Contact> test_contacts = check_for_phone(phone_original);

		System.out.println(test_contacts);

		while (test_contacts.size() == 0) {
			System.out.println("Sorry, that phone does not exist in the records!");
			System.out.println("\n" + "Please try another phone:");
			try {
				phone_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			test_contacts = check_for_phone(phone_original);
		}

		int map_id = 1;
		for (Contact c: test_contacts) {
			contact_list_map.put(map_id, c);
			map_id = map_id + 1;
		}
		
		if (test_contacts.size() > 1){
			System.out.println("There is more than one contact for the original phone.");
			print_list(test_contacts);


			System.out.println("\nSeparate ID will be added for identifying individual contact:");

			for (int i: contact_list_map.keySet()){
				int key = i;
				Contact value = contact_list_map.get(i);
				System.out.println(key + " : " + value.simplePrint());
			}

			System.out.println("\n" + "Please choose phone based on above list to select duplicate contact to edit:");
			try {
				id_duplicate = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			contactEdit = contact_list_map.get(id_duplicate);

			System.out.println("Here is your chosen contact:");
			System.out.println(id_duplicate + " : " + contactEdit.simplePrint());
		}
		else if (test_contacts.size() == 1) {
			contactEdit = test_contacts.get(0);
			System.out.println("Here is your chosen contact:");
			System.out.println(contactEdit.simplePrint());
		}

		

		System.out.println("\n" + "Please add new phone:");

		try {
			phone_new = reader.readLine();
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		test_contacts = check_for_phone(phone_new);

		while (test_contacts.size() >= 1) {
			System.out.println("\n\nSorry! There is more than one phone for this id.\n");
			System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
			print_list(test_contacts);
			System.out.println("\n\nPlease try another phone.");

			try {
				phone_new = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			test_contacts = check_for_phone(phone_new);

		}

		contactEdit.set_phone(phone_new);

		System.out.println("Phone changed! Here is the result:");

		print_one_contact(contactEdit);
	}

	// Return the_contacts ArrayList.
	public ArrayList<Contact> get_the_contacts() {

		return the_contacts;
	}

	// Beginning options for once the program is opened.	
	public void options() {
		System.out.println("\n\n1. Add a new contact");
		System.out.println("2. Delete a contact");
		System.out.println("3. Print out your contacts + info");
		System.out.println("4. Edit a contact.");
		System.out.println("5: Search for contact.");
		System.out.println("6. Quit the program");
	}

	/*
		The print_one_contact function is primarily used for displaying the result
		of an edit to a contact.
	*/
	public void print_one_contact(Contact c) {
		System.out.println("*--------*-------------*--------------*-------------*----------*-----------*---------*-----------*");
		System.out.println("|   ID   |  FirstName  |   LastName   |   Address   |   City   |   State   |   ZIP   |   Phone   |");
		System.out.println("*--------*-------------*--------------*-------------*----------*-----------*---------*-----------*");
		c.printContact();
		System.out.println("*------------------------------------------------------------------------------------------------*");
	}
	
	/*
		Print all contacts currently stored in the_contacts ArrayList.
	*/
	public void print_list(ArrayList<Contact> my_contacts) {
		// "ID, First Name, Last Name, Address, City, State, Zip, Phone Number"
		System.out.println("*--------*-------------*--------------*-------------*----------*-----------*---------*-----------*");
		System.out.println("|   ID   |  FirstName  |   LastName   |   Address   |   City   |   State   |   ZIP   |   Phone   |");
		System.out.println("*--------*-------------*--------------*-------------*----------*-----------*---------*-----------*");
		for (Contact c: my_contacts) {
			c.printContact();
			System.out.println("*------------------------------------------------------------------------------------------------*");
		}	
	}

	/*
		Each search function has an option for either searching for a single contact
		by component or searching for a range by component.

		If the choice is for one contact, the user is asked for the component, and the
		check_for function is used.

		If the choice is for a range, there is a range function created for each component.
		Loop through the_contacts checking for the component in question that fits within the range,
		and add it into an ArrayList which is returned.
	*/
	public void search_by_ID() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Contact contactEdit = null;
		int first_choice = -1;
		int id_original = -1;
		int id_low = -1;
		int id_high = -1;


		while (first_choice != 2) {
			System.out.println("\n\nID SEARCH:");
			System.out.println("Choose either number or range for id:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one ID.");

			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}


			// System.out.println("first_choice undecided: " + first_choice);
			// System.out.println("pre if statements first_choice: " + first_choice);			
			
			if (first_choice == 1) {
				System.out.println("first_choice 1: " + first_choice);
				System.out.println("Please choose an ID for the low end of the range:");
				
				try {
					id_low = Integer.parseInt(reader.readLine());
				} catch (Exception e) {
					System.out.println("Error Reading ID!");
					e.printStackTrace();
				}

				System.out.println("Choose an ID for the high end of the range:");
				
				try {
					id_high = Integer.parseInt(reader.readLine());
				} catch (Exception e) {
					System.out.println("Error Reading ID!");
					e.printStackTrace();
				}

				search_by_IDRange(id_low, id_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}

			System.out.println("first_choice 2: " + first_choice);
			System.out.println("Add ID: ");
			
			try {
				id_original = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading ID!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_id(id_original);
			

			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that ID does not exist in the records!");
			}
			else {
				print_list(test_contacts);
			}
			return;
		}
	}

	public void search_by_IDRange(int low, int high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			int i = c.get_ID();
			if (i >= low && i <= high) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_FirstName() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String first_name_original = new String();
		int first_choice = -1;
		String first_name_low = new String();
		String first_name_high = new String();
		
		while (first_choice != 2) {
			System.out.println("\n\nFIRST NAME SEARCH:");
			System.out.println("Choose either number or range for first name:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one first name.");

			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}


			System.out.println("first_choice undecided: " + first_choice);
			System.out.println("pre if statements first_choice: " + first_choice);			
			
			if (first_choice == 1) {
				System.out.println("first_choice 1: " + first_choice);
				System.out.println("Please choose a first name for the low end of the range:");
				
				try {
					first_name_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading First Name!");
					e.printStackTrace();
				}

				System.out.println("Choose a first name for the high end of the range:");
				
				try {
					first_name_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading First Name!");
					e.printStackTrace();
				}

				search_by_FirstNameRange(first_name_low, first_name_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}

			System.out.println("first_choice 2: " + first_choice);
			System.out.println("Add First Name: ");
			
			try {
				first_name_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading ID!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_first_name(first_name_original);
			
			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that First Name does not exist in the records!");
			}
			else {
				print_list(test_contacts);
			}
			return;
		}
	}
	
	public void search_by_FirstNameRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();
	
		for (Contact c: the_contacts) {
			String i = c.get_first_name();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_LastName() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String last_name_original = new String();
		int first_choice = -1;
		String last_name_low = new String();
		String last_name_high = new String();
		
		

		while (first_choice != 2) {
			System.out.println("\n\nLAST NAME SEARCH:");
			System.out.println("Choose either number or range for last name:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one last name.");
			
			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			if (first_choice == 1) {
				System.out.println("Please choose a last name for the low end of the range:");
				
				try {
					last_name_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading last name!");
					e.printStackTrace();
				}

				System.out.println("Choose a last name for the high end of the range:");
				
				try {
					last_name_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading last name!");
					e.printStackTrace();
				}

				search_by_LastNameRange(last_name_low, last_name_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}
			
			System.out.println("Add last name: ");
				
			try {
				last_name_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading last name!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_last_name(last_name_original);

			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that last name does not exist in the records!");
			}
			else {
				System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
				print_list(test_contacts);
				return;
			}
		}
	}
	
	public void search_by_LastNameRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			String i = c.get_last_name();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_Address() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String address_original = new String();
		int first_choice = -1;
		String address_low = new String();
		String address_high = new String();
		
		while (first_choice != 2) {
			System.out.println("\n\nADDRESS SEARCH:");
			System.out.println("Choose either number or range for id:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one ID.");
			
			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			if (first_choice == 1) {
				System.out.println("Please choose a address for the low end of the range:");
				
				try {
					address_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading address!");
					e.printStackTrace();
				}

				System.out.println("Choose a address for the high end of the range:");
				
				try {
					address_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading address!");
					e.printStackTrace();
				}

				search_by_AddressRange(address_low, address_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}
			
			System.out.println("Add address: ");
			
			try {
				address_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading address!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_address(address_original);

			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that address does not exist in the records!");
			}
			else {
				System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
				print_list(test_contacts);
				return;
			}
		}
	}
	
	public void search_by_AddressRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			String i = c.get_address();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_City() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String city_original = new String();
		int first_choice = -1;
		String city_low = new String();
		String city_high = new String();
		
		while (first_choice != 2) {
			System.out.println("\n\nCITY SEARCH:");
			System.out.println("Choose either number or range for id:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one ID.");
			
			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			if (first_choice == 1) {
				System.out.println("Please choose a city for the low end of the range:");
				
				try {
					city_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading city!");
					e.printStackTrace();
				}

				System.out.println("Choose a city for the high end of the range:");
				
				try {
					city_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading city!");
					e.printStackTrace();
				}

				search_by_CityRange(city_low, city_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}

			System.out.println("Add city: ");
			
			try {
				city_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading city!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_city(city_original);

			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that city does not exist in the records!");
			}
			else {
				System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
				print_list(test_contacts);
				return;
			}
		}
	}
	
	public void search_by_CityRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			String i = c.get_city();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_State() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String state_original = new String();
		int first_choice = -1;
		String state_low = new String();
		String state_high = new String();
		
		

		while (first_choice != 2) {
			System.out.println("\n\nSTATE SEARCH:");
			System.out.println("Choose either number or range for id:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one ID.");
			
			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}
			
			if (first_choice == 1) {
				System.out.println("Please choose a state for the low end of the range:");
				
				try {
					state_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading state!");
					e.printStackTrace();
				}

				System.out.println("Choose a state for the high end of the range:");
				
				try {
					state_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading state!");
					e.printStackTrace();
				}

				search_by_StateRange(state_low, state_high);
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}
			
			System.out.println("Add state: ");
			
			try {
				state_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading state!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_state(state_original);

			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that state does not exist in the records!");
			}
			else {
				System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
				print_list(test_contacts);
			}
		}
	}
	
	public void search_by_StateRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			String i = c.get_state();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_ZIP() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String zip_original = new String();
		int first_choice = -1;
		String zip_low = new String();
		String zip_high = new String();

		while (first_choice != 2) {
			System.out.println("\n\nZIP SEARCH:");
			System.out.println("Choose either number or range for id:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one ID.");
			
			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			if (first_choice == 1) {
				System.out.println("Please choose a zip for the low end of the range:");
				
				try {
					zip_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading zip!");
					e.printStackTrace();
				}

				System.out.println("Choose a zip for the high end of the range:");
				
				try {
					zip_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading zip!");
					e.printStackTrace();
				}

				search_by_ZipRange(zip_low, zip_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}
			
			System.out.println("Add zip: ");
				
			try {
				zip_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading zip!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_zip(zip_original);


			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that zip does not exist in the records!");
			}
			else {
				System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
				print_list(test_contacts);
				return;
			}
		}
	}
	
	public void search_by_ZipRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			String i = c.get_zip();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	public void search_by_Phone() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Contact contactEdit = null;
		String phone_original = new String();
		int first_choice = -1;
		String phone_low = new String();
		String phone_high = new String();
		
		

		while (first_choice != 2) {
			System.out.println("\n\nPHONE SEARCH:");
			System.out.println("Choose either number or range for id:");
			System.out.println("1: Search for range.");
			System.out.println("2: Search for one ID.");
			
			try {
				first_choice = Integer.parseInt(reader.readLine());
			} catch (Exception e) {
				System.out.println("Error Reading Choice!");
				e.printStackTrace();
			}

			if (first_choice == 1) {
				System.out.println("Please choose a phone for the low end of the range:");
				
				try {
					phone_low = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading phone!");
					e.printStackTrace();
				}

				System.out.println("Choose a phone for the high end of the range:");
				
				try {
					phone_high = reader.readLine();
				} catch (Exception e) {
					System.out.println("Error Reading phone!");
					e.printStackTrace();
				}

				search_by_PhoneRange(phone_low, phone_high);
				return;
			}
			else if (first_choice != 2) {
				System.out.println("ERROR: INVALID CHOICE. Please choose either '1' or '2'\n\n");
			}
			
			System.out.println("Add phone: ");
			
			try {
				phone_original = reader.readLine();
			} catch (Exception e) {
				System.out.println("Error Reading phone!");
				e.printStackTrace();
			}
			
			ArrayList<Contact> test_contacts = check_for_phone(phone_original);

			if (test_contacts.size() == 0) {
				System.out.println("Sorry, that phone does not exist in the records!");
			}
			else {
				System.out.println("\n\n\nFOUND " + test_contacts.size() + " RECORDS.\n\n");
				print_list(test_contacts);
				return;
			}
		}
	}

	public void search_by_PhoneRange(String low, String high) {
		ArrayList<Contact> range = new ArrayList<Contact>();

		for (Contact c: the_contacts) {
			String i = c.get_phone();
			if ((i.compareTo(low) > 0 && i.compareTo(high) < 0) || i.equals(low) || i.equals(high)) {
				range.add(c);
			}
		}

		System.out.println("Showing Contact Records for IDs in range: " + "(" + low + ", " + high + ")\n\n\n");

		print_list(range);
		System.out.println("\n\n");

		System.out.println("Returning back to Search...\n\n");
	}

	// All options for the search feature of the program.
	public void search_options() {
		System.out.println("\n\nSEARCH CONTACT:");
		System.out.println("Please select choice/method to search contact:");
		System.out.println("1: ID");
		System.out.println("2: First Name");
		System.out.println("3: Last Name");
		System.out.println("4: Address");
		System.out.println("5: City");
		System.out.println("6: State");
		System.out.println("7: Zip");
		System.out.println("8: Phone Number");
		System.out.println("9: Exit search");
	}

	/*
		search_contact functions in the exact same manner as edit_contact,
		except it stops with finding the contact by component and not changing
		the contact.
	*/
	public void search_contact() {
		search_options();

		int choice = -1;
		int delete_id = -1;

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			choice = Integer.parseInt(reader.readLine());	
		} catch (Exception e) {
			System.out.println("Error Reading Choice!");
			e.printStackTrace();
		}

		while (choice != 9) {
			if (choice == 1) {
				search_by_ID();
			}
			else if (choice == 2) {
				search_by_FirstName();
			}
			else if (choice == 3) {
				search_by_LastName();
			}
			else if (choice == 4) {
				search_by_Address();
			}
			else if (choice == 5) {
				search_by_City();
			}
			else if (choice == 6) {
				search_by_State();
			}
			else if (choice == 7) {
				search_by_ZIP();
			}
			else if (choice == 8) {
				search_by_Phone();
			}
			else if (choice != 9) {
				System.out.println("Invalid choice.");
			}
			search_options();
			try {
				choice = Integer.parseInt(reader.readLine());	
			} catch (Exception e) {
				System.out.println("Error AGAIN!!!");
				e.printStackTrace();
			}
		}
	}

	/*
		set_contacts changes the_contacts to the contact ArrayList inputted into
		the function.
	*/
	public void set_contacts(ArrayList<Contact> contact_list) {
		
		the_contacts = contact_list;
	}

	/*
		start is based on the same structure as search_contact and edit_contact,
		and designed around the options function's listed features.
	*/
	public void start() {
		int choice = -1;
		int delete_id = -1;
		
		options();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			choice = Integer.parseInt(reader.readLine());	
		} catch (Exception e) {
			System.out.println("Error AGAIN!!!");
			e.printStackTrace();
		}
		while (choice != 6) {
			if (choice == 1) {
				add_contact();
			}
			else if (choice == 2) {
				System.out.println("Enter the id of the contact you wish to delete:");
				try {
					delete_id = Integer.parseInt(reader.readLine());
				} catch (Exception e) {
					System.out.println("Error AAAAGAIN!!!");
					e.printStackTrace();
				}
				delete_contact(delete_id);
			}
			else if (choice == 3) {
				HashMap <Integer, Integer> duplicate_check_map = this.check_for_duplicates();
				if (duplicate_exists == true) {
					System.out.println("WARNING: Duplicates have been found. Records cannot be sorted.\n\n");
					print_list(the_contacts);
				}
				else	{
					quicksort(the_contacts, 0, the_contacts.size() - 1 );
					System.out.println("\nDatabase sorted by ID first...\n\n");
					print_list(the_contacts);
				}
			}
			else if (choice == 4) {
				edit_contact();
			}
			else if (choice == 5) {
				search_contact();
			}
			else if (choice != 6) {
				System.out.println("Invalid choice.");
			}
			options();
			try {
				choice = Integer.parseInt(reader.readLine());	
			} catch (Exception e) {
				System.out.println("Error AGAIN!!!");
				e.printStackTrace();
			}
		}
	}

	/*
		Designate the filename where the csv file is located.

		Set up the CSV_readwrite object to read and write to the csv file.

		Set the Contact_List's contact list to that in the csv file, check
		for duplicates, confirm that the contacts have been read, and output
		the start options.

		Once the user exits, write the Contact_List's contacts to the csv file.
	*/
	public static void main( String [] args ) {

		String fileName = System.getProperty("user.home")+"/contact_list.csv";
		
		Contact_List my_list = new Contact_List();

		System.out.println("my_list initialized...");
		
		// ArrayList<Contact> init_contact_list = new ArrayList<Contact>();

		// System.out.println("init_contact_list initialized...");

		System.out.println("Going to read contact_list.csv file...");
		my_list.set_contacts(CSV_readwrite.csv_reader(fileName, my_list.get_the_contacts()));

		HashMap <Integer, Integer> duplicate_check_map = my_list.check_for_duplicates();

		ArrayList<Contact> current_contacts = my_list.get_the_contacts();

		if (current_contacts.size() > 0) {
			System.out.println("Contacts read from contact_list.csv!");
		}
		else {
			my_csv = new CSV_readwrite(fileName);
		}

		System.out.println("\n\n\nWelcome to Contact List.");
		System.out.println("You have the following choices:");
		my_list.start();

		current_contacts = my_list.get_the_contacts();

		CSV_readwrite.csv_writer(current_contacts);
	}
}
