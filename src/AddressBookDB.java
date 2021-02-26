import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * Alexander Aghili
 * COMP 232
 * 
 */
public class AddressBookDB {
	
	private final String tableCreationStatement = "create table contacts (" + 
			"	id INT NOT NULL AUTO_INCREMENT," + 
			"    firstName VARCHAR(100)," + 
			"    lastName VARCHAR(100)," + 
			"    phoneNumber VARCHAR(16)," + 
			"    address VARCHAR(100)," + 
			"    PRIMARY KEY(ID)" + 
			");";
	
	
	private ArrayList<Contact> contactList = new ArrayList<Contact>();
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private PreparedStatement preparedStatement = null;
	
	private String username = "root";
	private String password = "********";
	
	public AddressBookDB() throws ClassNotFoundException{
		//Creates the database if it doesn't exist, otherwise it will throw an SQLException in which I will do nothing, since it already exists.
		try {
			makeDatabase();
			
		} catch (ClassNotFoundException e) { //There is an issue with the driver itself
			e.printStackTrace();
			throw e;
		} catch (SQLException se) { //There is an issue with the statement, likely because the database already exists. This assumes it does and continues.
		} finally {
			close();
		}
		
		
	}
	
	//Creates database if non exists
	private void makeDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/", username, password);
		statement = connect.createStatement();
		statement.executeUpdate("CREATE DATABASE ContactDB;");
		statement.executeUpdate("USE ContactDB;");
		
		//Creates the table contacts to store all the contact information
		statement.executeUpdate(tableCreationStatement);
	}
	
	
	
	public void setContacts(ArrayList<Contact> contacts) throws ClassNotFoundException, SQLException{
		contactList = contacts;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/ContactDB", username, password);
			statement = connect.createStatement();
			statement.executeUpdate("DROP TABLE contacts;");
			statement.executeUpdate(tableCreationStatement);
			
			for (Contact contact: contactList) {
				
				preparedStatement = connect.prepareStatement("insert into ContactDB.contacts"
						+ " values(default, ?, ?, ?, ?)");
				
				preparedStatement.setString(1, contact.getFirstName());
				preparedStatement.setString(2, contact.getLastName());
				preparedStatement.setString(3, contact.getPhoneNumber());
				preparedStatement.setString(4, contact.getAddress());
				
				preparedStatement.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException se) {
			throw se;
		} finally {
			close();
		}
		
	}
	
	
	public ArrayList<Contact> getContacts() throws ClassNotFoundException, SQLException {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/ContactDB", username, password);
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from ContactDB.contacts");
			
			while(resultSet.next()) {
				Contact contact = new Contact(resultSet.getString("firstName"),
						resultSet.getString("lastName"), 
						resultSet.getString("phoneNumber"), 
						resultSet.getString("address"));
				contactList.add(contact);
			}
			
			
			
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException se) {
			throw se;
		} finally {
			close();
		}

		return contactList;
	}
	
	private void close() {
		try {
			if(resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
