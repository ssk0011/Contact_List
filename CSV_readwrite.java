import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

/**
*	Partly based on:
*	http://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
*/

public class CSV_readwrite {
	/*
		Store the official filename and a separate FileWriter.
	*/
	private static String official_filename;
	private static FileWriter w;

	// Constructor
	public CSV_readwrite(String csvname) {
	
		FileWriter w = null;
				
		try {
			/*
				Function reads in a filename for the csv file, then creates the
				file and appends the column headers to the first line.
			*/
			w = new FileWriter(csvname);
			official_filename = csvname;
			w = w;
			w.append("ID, First Name, Last Name, Address, City, State, Zip, Phone Number");
			w.append("\n");
			
			System.out.println("CSV file has been initialized.");
			
		} catch (Exception e) {
			System.out.println("ERROR: Initializing CSV!");
			e.printStackTrace();
		} finally {
			try {
				w.flush();
				w.close();
			} catch (IOException e) {
				System.out.println("ERROR: Flushing/closing CSV writer in initialization!");
				e.printStackTrace();
			}
			
		}
	}

	public static void one_csv_write (Contact c) {
		
		// Initialize empty filewriter.
		FileWriter w = null;
				
		try {
			/*
				Set up the filewriter based on stored official filename,
				then append the given contact record, result being:

				"ID,First Name,Last Name,Address,City,State,ZIP,Phone"
			*/
			w = new FileWriter(official_filename, true);
			w.append(Integer.toString(c.getID()));
			System.out.println(c.getFirstName());

			w.append(",");
			w.append(c.getFirstName());
			w.append(",");
			w.append(c.getLastName());
			w.append(",");
			w.append(c.getAddress());
			w.append(",");
			w.append(c.getCity());
			w.append(",");
			w.append(c.getState());
			w.append(",");
			w.append(c.getZip());
			w.append(",");
			w.append(c.getPhone());
			w.append("\n");

			System.out.println("Data successfully appended to CSV.");
			
		} catch (Exception e) {
			System.out.println("ERROR: Appending data to CSV!");
			e.printStackTrace();
		}
		finally {
			
			try {
				w.flush();
				w.close();
			} catch (IOException e) {
				System.out.println("ERROR: Flushing/closing CSV Writer while appending data!");
				e.printStackTrace();
			}
			
		}
	}

	public static void csv_writer (ArrayList<Contact> contacts) {
				
		try {
			/*
				Set up the filewriter based on stored official filename,
				then append all contact records from the list, result being:

				"ID,First Name,Last Name,Address,City,State,ZIP,Phone"
			*/

			w = new FileWriter(official_filename);
			w.append("Number,GivenName,Surname,StreetAddress,City,State,ZipCode,Telephone");
			w.append("\n");
			
			for (Contact c : contacts) {
				w.append(Integer.toString(c.getID()));
				w.append(",");
				w.append(c.getFirstName());
				w.append(",");
				w.append(c.getLastName());
				w.append(",");
				w.append(c.getAddress());
				w.append(",");
				w.append(c.getCity());
				w.append(",");
				w.append(c.getState());
				w.append(",");
				w.append(c.getZip());
				w.append(",");
				w.append(c.getPhone());
				w.append("\n");
			}
			
			System.out.println("CSV file was created! Goodbye!");
			
		} catch (Exception e) {
			System.out.println("ERROR: Writing CSV File.");
			e.printStackTrace();
		}
		finally {
			
			try {
				w.flush();
				w.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing csv_writer !!!");
				e.printStackTrace();
			}
			
		}
	}
	
	public static ArrayList<Contact> csv_reader(String csvname, ArrayList<Contact> contacts) {
		/*
			Begin by reading the csv file to check for existing records.
		*/
		BufferedReader r = null;
		
		try {
			
			String line = "";
			r = new BufferedReader(new FileReader(csvname));
			official_filename = csvname;
			
			/*
				Skip the first line of the csv file with column titles
				by reading it before analyzing the records.
			*/
			r.readLine();

			/*
				After the column titles have been read, for each new line,
				check for the contact record and split the line into individual
				components to be placed into a Contact object.

				Then, add this Contact c into the contacts ArrayList previously
				passed in.
			*/
			while ((line = r.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 0) {
					Contact c = new Contact(Integer.parseInt(tokens[0]), 
						tokens[1], tokens[2], 
						tokens[3], tokens[4], tokens[5], 
						tokens[6], tokens[7]);
					contacts.add(c);
				}
			}
			System.out.println("Read " + contacts.size() + " contact records.");
	   	} 
		catch (Exception e) {
			System.out.println("CSV File Does Not Exist.");
			e.printStackTrace();
			csv_writer(contacts);
		}
		finally {
			try {
				r.close();
			} catch (IOException e) {
				System.out.println("Error while closing csv_reader !!!");
				e.printStackTrace();
			}
		}
		return contacts;
	}

}
