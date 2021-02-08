//----------------------------------------------------------------------------------------------
/* Alexander Aghili
* COMP 232
* February 8th, 2021
*/
//----------------------------------------------------------------------------------------------


//----------------------------------------------------------------------------------------------
/*Contact class, is a single contact with all the proper information that a contact has.
* The following information is contained in a contact:
* First Name as a String
* Last Name as a String
* Phone Number as a String, formatted as follows: +x (xxx) xxx-xxxx if no +x is added,
*	it is assume to be American and therefore +1
* Address as a String
*/
//----------------------------------------------------------------------------------------------
public class Contact implements Comparable<Contact>{

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	
	public Contact(String firstName, String lastName, String phoneNumber, String address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		return "\n First Name: " + firstName + "\n Last Name: " + lastName + 
				"\n Phone Number: " + phoneNumber + "\n Address : " + address; 
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Contact) {
			Contact newContact = (Contact) obj;
			if (this.firstName.equals(newContact.getFirstName()) &&
					this.lastName.equals(newContact.getLastName()))
				return true;
			else
				return false;
		} 
		else
			return false;
	}
	
	@Override
	public int compareTo(Contact o) {
		if (this.lastName.equals(o.lastName)) {
			if (this.firstName.equals(o.firstName)) {
				return this.phoneNumber.compareTo(o.phoneNumber);
			} else
				return this.firstName.compareTo(o.firstName);
		} else 
			return this.lastName.compareTo(o.lastName);
	}
	
}
