import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Contact {
	// All components of a contact record.
	private int id;
	private String first_name;
	private String last_name;
	private String address;
	private String city;
	private String state;
	private String zip_code;
	private String phone_number;

	// Constructor
	public Contact(int i, String f, String l, String a, String c, 
		String st, String zip, String phone) {
		
		/*
			For each component of the record, create a value based on which
			values are inputted into the constructor. Handle cases for when
			a value does not exist.
		*/
		if (i == 0) id = 0;
		else id = i;
		if (f != null) first_name = f;
		else first_name = "";
		if (l != null) last_name = l;
		else last_name = "";
		if (a != null) address = a;
		else address = "";
		if (c != null) city = c;
		else city = "";
		if (st != null) state = st;
		else state = "";
		if (zip != null) zip_code = zip;
		else zip_code = "000";
		if (phone != null) phone_number = phone;
		else phone = "";	
	}
	
	public void printContact() {
		/*
			Start off with a set_ of empty strings for each aspect of the
			contact record.
		*/
		String r1 = "";
		String r2 = "";
		String r3 = "";
		String r4 = "";
		String r5 = "";
		String r6 = "";
		String r7 = "";
		String r8 = "";

		/*
			For each component of the contact record:

			The final strings are based on the database UI below:
				
				8		 13			14			13		  10		    11		9		11
			*--------*-------------*--------------*-------------*----------*-----------*---------*-----------*
			|   ID   |  first_name  |   last_name   |   address   |   city   |   state   |   ZIP   |   phone   |
			*--------*-------------*--------------*-------------*----------*-----------*---------*-----------*

			The number above each field is the maximum string length that
			can exist underneath it.

			After determining the length of each string, compare it to the
			maximum length of each field, and cut the string accordingly, along
			with adding a "..." if it exceeds the maximum length.
		*/
		String id_string = Integer.toString(id);
		int id_length = id_string.length();
		String id_sub = id_string;

		if (id_length > 8) {
			id_sub = id_string.substring(0, 8-3);
			id_sub = id_sub.concat("...");
		}
		else {
			r1 = new String(new char[8-id_length]).replace("\0", " ");
		}

		int first_name_length = first_name.length();
		String first_name_sub = first_name;

		if (first_name_length > 13) {
			first_name_sub = first_name.substring(0, 13-3);
			first_name_sub = first_name_sub.concat("...");
		}
		else {
			r2 = new String(new char[13-first_name_length]).replace("\0", " ");
		}

		int last_name_length = last_name.length();
		String last_name_sub = last_name;

		if (last_name_length > 14) {
			last_name_sub = last_name.substring(0, 14-3);
			last_name_sub = last_name_sub.concat("...");
		}
		else {
			r3 = new String(new char[14-last_name_length]).replace("\0", " ");
		}

		int address_length = address.length();
		String address_sub = address;

		if (address_length > 13) {
			address_sub = address.substring(0, 13-3);
			address_sub = address_sub.concat("...");
		}
		else {
			r4 = new String(new char[13-address_length]).replace("\0", " ");
		}

		int city_length = city.length();
		String city_sub = city;

		if (city_length > 10) {
			city_sub = city.substring(0, 10-3);
			city_sub = city_sub.concat("...");
		}
		else {
			r5 = new String(new char[10-city_length]).replace("\0", " ");
		}

		int state_length = state.length();
		String state_sub = state;

		if (state_length > 11) {
			state_sub = state.substring(0, 11-3);
			state_sub = state_sub.concat("...");
		}
		else {
			r6 = new String(new char[11-state_length]).replace("\0", " ");
		}

		String zip_code_string = zip_code;
		int zip_code_length = zip_code_string.length();
		String zip_code_sub = zip_code_string;

		if (zip_code_length > 9) {
			zip_code_sub = zip_code_string.substring(0, 9-3);
			zip_code_sub = zip_code_sub.concat("...");
		}
		else {
			r7 = new String(new char[9-zip_code_length]).replace("\0", " ");
		}

		int phone_length = phone_number.length();
		String phone_sub = phone_number;

		if (phone_length > 11) {
			phone_sub = phone_number.substring(0, 11-3);
			phone_sub = phone_sub.concat("...");
		}
		else {
			r8 = new String(new char[11-phone_length]).replace("\0", " ");
		}

		System.out.println("|" + id_sub + r1 + "|" + 
			first_name_sub + r2 + "|" + last_name_sub + r3 + 
			"|" + address_sub + r4 + "|" + city_sub + r5 + "|" + 
			state_sub + r6 + "|" + zip_code_sub + r7 + "|" + 
			phone_sub + r8 + "|");	
	}

	// Return each element of the contact record as a string.
	public String print_one_line() {
		return Integer.toString(this.get_ID()) + ", " + this.get_first_name() + 
		", " + this.get_last_name() + ", " + this.get_address() + ", " + 
		this.get_city() + ", " + this.get_state() + ", " + this.get_zip() 
		+ ", " + this.get_phone(); 
	}
	
	public String simplePrint() {
		return id + " | " + first_name + " | " + last_name + " | " + 
		address + " | " + city + " | " + state + " | " + zip_code + " | " + 
		phone_number;
	}
	
	/*
		Here are all functions for get_ting and set_ting values for all
		elements of the contact record.
	*/
	public int get_ID(){ return id; }
	public String get_first_name() { return first_name; }
	public String get_last_name() { return last_name; }
	public String get_address() { return address; }
	public String get_city() { return city; }
	public String get_state() { return state; }
	public String get_zip() { return zip_code; }
	public String get_phone() { return phone_number; }
	public void set_ID(int new_id){ id = new_id; }
	public void set_first_name(String new_first_name) { first_name = new_first_name; }
	public void set_last_name(String new_last_name) { last_name = new_last_name; }
	public void set_address(String new_address) { address = new_address; }
	public void set_city(String new_city) { city = new_city; }
	public void set_state(String new_state) { state = new_state; }
	public void set_zip(String new_zip) { zip_code = new_zip; }
	public void set_phone(String new_phone) { phone_number = new_phone; }	

	
}

