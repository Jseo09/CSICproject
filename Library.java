import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Library {
    private final String ADDRESS;
    private ArrayList<Book> books = new ArrayList<>();


    /**
     ---------------------------------------------------------------
     AUTHOR:         JIN SEO
     CREATED DATE:   2023/11/15
     PURPOSE:        Basic constructor for the library class; This will create the library with the specific address given
     PRECONDITIONS:  N/A
     POSTCONDITIONS: Constructs the library object
     ARGUMENTS:      Requires the String address from the user
     DEPENDENCIES:   N/A
     ---------------------------------------------------------------
     */
    public Library(String address) {
        this.ADDRESS = address;
    }
    /**
     ---------------------------------------------------------------
     AUTHOR:         JIN SEO
     CREATED DATE:   2023/11/15
     PURPOSE:        Basic constructor for the library class; This will create the library with the specific address given and get the book titles from file_title that is given
                    by the users
     PRECONDITIONS:  Change the file directory inside of the method for the successful compile
     POSTCONDITIONS: Library object is constructed with specific file for the books
     ARGUMENTS:      Requires the String address from the user and the file title from the user
     DEPENDENCIES:   addBooksFromCSV method
     ---------------------------------------------------------------
     */
    public Library(String address, String file_title) {
        this.ADDRESS = address;
        /*
        !!!!IMPORTANT!!!!
        Change this directory since the file directory depends on csv file locations
        */
        String file_directory = "out/production/Project_for_CSIC/out/production/Project_for_CSIC/" + file_title;
        addBooksFromCSV(file_directory);

    }
    /**
     ---------------------------------------------------------------
     AUTHOR:         JIN SEO
     CREATED DATE:   2023/11/15
     PURPOSE:        To add the book object into the book array
     PRECONDITIONS:  Must have the book array
     POSTCONDITIONS: Adds additional book object
     ARGUMENTS:      Requires the Book type object
     DEPENDENCIES:   add method
     ---------------------------------------------------------------
     */

    public void addBook(Book book) {
        books.add(book);
    }
    /**
     --------------------------------------------------------------
     AUTHOR:         JIN SEO & Juliet
     CREATED DATE:   2023/11/15
     PURPOSE:        The method will count those books that has matching bookTitles
     PRECONDITIONS:  Requires the book array
     POSTCONDITIONS: @return the counts to the functions
     ARGUMENTS:      Requires the Book type object
     DEPENDENCIES:   add method

     Example: The Lord of the Rings, remaining numbers of copies : 0
     used the get count method professor suggested for us
     ---------------------------------------------------------------
     */

    /*this is counting the books that is available
     */
    public int[] get_count(String bookTitle) {
        int availableCount = 0;
        int totalCount = 0;

        for (Book book : books) {
            if (book.getTitle().equals(bookTitle)) {
                totalCount++;
                if (!book.isBorrowed()) {
                    availableCount++;
                }
            }
        }
        int[] counts = {availableCount, totalCount};
        return counts;
    }

    /**
    ---------------------------------------------------------------
    AUTHOR:         JIN SEO
    CREATED_DATE:   2023/10/25
    PURPOSE:        Method will borrow the book from the library
    PRECONDITIONS:  Must have at least one library object instanciated
    POSTCONDITIONS: Will remove one book object count with the same title with the argument String, therefore, when counting,the book will not be counted anymore unless it is returned
    ARGUMENTS:      Requires String "Booktitle"  Can be NULL
    DEPENDENCIES:   Method getCount

    Example:
    You successfully borrowed 'The Lord of the Rings', remaining copies are 1
    You successfully borrowed 'The Lord of the Rings', remaining copies are 0
    -----------------------------------------------------------------
    */
    // modified borrowBook method in order to use the updated get count method and handle the array of the two integers correctly
    public void borrowBook(String bookTitle) {
        boolean find = false;
        //this will receive the available book's counts
        int[] counts = get_count(bookTitle);
        //count[0] = available book && count[1] = total_books
        //for the book in the books array this will get the books with the same title that has not been borrowed already
        //If the books are founded, we set the find as true and set the founded book from the array as rented and break the loop
        if (counts[1] == 0)
            System.out.println("The book '" + bookTitle + "' is not in our catalog.");
        else if(counts[0] == 0)
            System.out.println("Sorry, no more copies of '" + bookTitle + "' are available at this moment");
        else {
            for (Book book : books) {
                //if the books with the same title and is borrowed
                if (book.getTitle().equals(bookTitle) && !book.isBorrowed()) {
                    book.rent();
                    counts[0] = counts[0]-1;
                    System.out.println("You successfully borrowed '" + bookTitle + "', remaining copies are " + counts[0]);
                    break;
                }
            }
        }
    }
    /**
     ---------------------------------------------------------------
     AUTHOR:         JIN SEO
     CREATED_DATE:   2023/10/25
     PURPOSE:        Method will print the available book in the arraylist
     PRECONDITIONS:  Must have at least one library object and book array instanciated
     POSTCONDITIONS: No Change
     ARGUMENTS:      N/A
     DEPENDENCIES:   Method get_count

     Example:
     Foundations of Human Sociality, remaining numbers of copies : 1
     Halting Degradation of Natural Resources, remaining numbers of copies : 1
     -----------------------------------------------------------------
     */
    // This will print the available books in the arraylist.
    // also modified this method to use the updated get count method
    public void printAvailableBooks() {
        // this will keep the title of the books so when program print out the left over available book counts for specific book title,
        ArrayList<String> used = new ArrayList<>();
        for (Book book : books) {
            String bookTitle = book.getTitle();
            // Get the available book counts
            int[] count = get_count(book.getTitle());
            // if the book title is not duplicate, get the book title and put it with the count
            if (!used.contains(bookTitle)) {
                used.add(bookTitle);
                System.out.println(bookTitle + ", remaining numbers of copies : " + count[0]);
            }
        }
        // if there is no book, there is no book in catalog
        if (used.isEmpty())
            System.out.println("No book in catalog");
    }

    public void printAddress() {
        System.out.println(ADDRESS);
    }


    /**
     ---------------------------------------------------------------
     AUTHOR: Hayden Sutton & Jin Seo
     CREATED_DATE: 2023/11/05
     PURPOSE: The method will return the borrowed book to the library
     PRECONDITIONS: Must have at least one library object instantiated and an array of book objects
     POSTCONDITIONS: Updates the status of the book object as returned and increments the count of available copies
     ARGUMENTS: Requires String "bookTitle" which is the title of the book to return. Can be NULL.
     DEPENDENCIES: Relies on method get_count to get the current count of books and whether the book is borrowed

     Example:
     Returning 'The Lord of the Rings'
     You successfully returned the book, 'The Lord of the Rings.' The current copies of the book are: 2
    ---------------------------------------------------------------
    */
    //method to return the book to the library/
    public void returnBook(String bookTitle) {
        int[] counts = get_count(bookTitle);
        if (counts[0] < counts[1]) {
            for (Book book : books) {
                if (book.getTitle().equals(bookTitle) && book.isBorrowed()) {
                    book.returned();
                    System.out.println("Returning " + bookTitle);
                    break;
                }
            }
            System.out.println("You successfully returned the book, " + "'" + bookTitle + ".'" + "The current copies of the book are : " + (counts[0] + 1));
        } else {
            System.out.println("Book was not borrowed or is not in the catalog.");
        }
    }


    /**
     ---------------------------------------------------------------
     AUTHOR: JIN SEO using ChatGPT
     CREATED_DATE: 2023/10/25
     PURPOSE: This method reads data from a CSV file containing a list of book titles and their respective number of copies, and returns it as a list of string arrays.
     PRECONDITIONS: The CSV file must be in the correct format with two fields per line, the first being the book title and the second the number of copies.
     POSTCONDITIONS: Produces a list where each element is a string array of two elements: the book title and the number of copies available.
     ARGUMENTS: String "filename" which is the path to the CSV file containing the book inventory.
     DEPENDENCIES: Relies on Java's IO classes for reading the file contents.

     Example:
     For a CSV file 'CATALOG.csv' with the content:
     "The Countess of Huntingdon's Connexion",1
     "Economic Organizations and Corporate Governance in Japan",2
     The call readCSV("library_inventory.csv") will return:
     [["The Countess of Huntingdon's Connexion", "1"], ["Economic Organizations and Corporate Governance in Japan", "2"]]
    ---------------------------------------------------------------
    */
    //From ChatGPT to import CSV file into the java file
    private List<String[]> readCSV(String filename) {
        List<String[]> data = new ArrayList<>();
        //reads each line of the CSV file using BufferReader
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            //when they did not finish reading the line
            while ((line = br.readLine()) != null) {
                // Split the line into an array of values using a comma as the delimiter
                String[] values = line.split(",");
                data.add(values);
            }
            //catch the exceptions, and print the error if it happens
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     ---------------------------------------------------------------
     AUTHOR:         JIN SEO using ChatGPT
     CREATED_DATE:   2023/10/25
     PURPOSE:        This method reads data from a CSV file and adds books to a collection based on the information in the CSV.
     PRECONDITIONS:  CSV file should be specified(fileDirectory should exist), CSV file should have rows with at least two elements: title and counts
     POSTCONDITIONS: Books from the CSV file are added to the collection
     ARGUMENTS:      fileDirectory: the directory path of the CSV file containing the book information
     DEPENDENCIES:   readCSV method, Book class should be defined, addBook method should be defined

     -----------------------------------------------------------------
     */
    private void addBooksFromCSV(String fileDirectory) {
        //get the data from the method redCSV, and create list out of it
        List<String[]> csvData = readCSV(fileDirectory);

        for (String[] row : csvData) {
            if (row.length >= 2) {
                String bookTitle = row[0];
                //parse the integer
                int count = Integer.parseInt(row[1]);

                for (int i = 0; i < count; i++) {
                    Book book = new Book(bookTitle);
                    addBook(book);
                }
            }
        }
    }

    public static void printOpeningHours() {
        System.out.println("Libraries are open daily from 9am to 5pm.\n");
    }

    public static void main(String[] args) {
        // Create libraries
        System.out.println();
        Library firstLibrary = new Library("10 Main St.");
        Library secondLibrary = new Library("228 Liberty St.");
        Library thirdLibrary = new Library("12 Broadway St.", "catalog.csv");

        // Add four books to the first library
        firstLibrary.addBook(new Book("The Da Vinci Code"));
        firstLibrary.addBook(new Book("The Da Vinci Code")); // second copy
        firstLibrary.addBook(new Book("Le Petit Prince"));
        firstLibrary.addBook(new Book("A Tale of Two Cities"));
        firstLibrary.addBook(new Book("The Lord of the Rings"));
        firstLibrary.addBook(new Book("The Lord of the Rings")); // second copy

        // Print opening hours and the addresses
        System.out.println("Library hours:");
        printOpeningHours();
        System.out.println();
        System.out.println("Library addresses:");
        firstLibrary.printAddress();
        secondLibrary.printAddress();
        thirdLibrary.printAddress();
        System.out.println();

        // Try to borrow The Lords of the Rings from both libraries
        firstLibrary.borrowBook("The Lord of the Rings");
        firstLibrary.borrowBook("The Lord of the Rings");
        firstLibrary.borrowBook("The Lord of the Rings");
        System.out.println();

        // Print the titles of all available books from both libraries
        System.out.println("Books available in the first library:");
        firstLibrary.printAvailableBooks();
        System.out.println();
        System.out.println("Books available in the second library:");
        secondLibrary.printAvailableBooks();
        System.out.println();
        System.out.println("Books available in the third library:");
        thirdLibrary.printAvailableBooks();
        System.out.println();

        // Return The Lords of the Rings to the first library
        firstLibrary.returnBook("The Lord of the Rings");
        System.out.println();
        // Print the titles of available from the first library
        System.out.println("Books available in the first library:");
        firstLibrary.printAvailableBooks();
    }

}
