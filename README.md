# AddressBook
I have updated the Address Book on Febuary 26, 2021, without properly creating a new branch. Therefore, the main branch contains Address Book II and the Address-Book-I branch contains Address Book I. If you need to access address book I, change the branch to Address-Book-I
Otherwise, the following changes are required in the AddressBookDB class to ensure it runs on your device:
1. Alter the MySQL connection if required. Mine(the default) is set up as jdbc:mysql://localhost:3306/ but if this is not the proper connection, alter the text on line 54, and alter the text before ContactDB on line 68 and 100.
2. Alter the username by changing the text in the username String variable on line 32.
3. Alter the password by changing the text in the password String variable on line 33.
Once these changes have occured the program should work properly. There is no need to create your own database or table, the program does this automatically.
