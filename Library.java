import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Library {
    private final String ADDRESS;
    private ArrayList<Book> books = new ArrayList<>();

    public Library(String address) {
        this.ADDRESS = address;
    }

    public Library(String address, String file_title) {
        this.ADDRESS = address;
        // !!!!IMPORTANT!!!!
        // Change this directory since the file directory depends on csv file locations*
        String file_directory = "out/production/Project_for_CSIC/ " + file_title;
        addBooksFromCSV(file_directory);

    }

    public void addBook(Book book) {
        books.add(book);
    }

    /*this is counting the books that is available
    Example: The Lord of the Rings, remaining numbers of copies : 0 */
    // used the get count method professor suggested for us
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
        int [] counts = {availableCount, totalCount};
        return counts;
    }

    /*
    ---------------------------------------------------------------
    AUTHOR:         JIN SEO
    CREATED_DATE:   2023/10/25
    PURPOSE:        Method will borrow the book from the library
    PRECONDITIONS:  Must have at least one library object instanciated
    POSTCONDITIONS: No Change
    ARGUMENTS:      Requires String "Booktitle"  Can be NULL
    DEPENDENCIES:   Method getCount

    Example:
    You successfully borrowed 'The Lord of the Rings', remaining copies are 1
    You successfully borrowed 'The Lord of the Rings', remaining copies are 0
    -----------------------------------------------------------------
    */
    // modified borrowBook method in order to use the updated get count method and handle the array of the two integers correctly
    public void borrowBook(String bookTitle) {
        boolean borrowed = false;
        boolean find = false;
        //this will receive the available book's counts
        int[] counts = get_count(bookTitle);
        int availableCount = counts[0];
        int totalCount = counts[1];

        // for the book in the books array this will get the books with the same title that has not been borrowed already
        // If the books are founded, we set the find as true and set the founded book from the array as rented and break the loop
        if (availableCount > 0) {
            for (Book book : books) {
                //if the books with the same title nad
                if (book.getTitle().equals(bookTitle) && !book.isBorrowed()) {
                    find = true;
                    book.rent(book);
                    availableCount--;
                    break;
                }
            }
        }

        // this functions will show whether the books were successfully borrowed or not.
        // If the book was not found as an available book, check if it is in the library by checking its title and state that
        // it has been already borrowed and cannot borrow anymore.
        // If it is not in the catalog, then it will print out that the book is not in the catalog
        if (find) {
            System.out.println("You successfully borrowed '" + bookTitle + "', remaining copies are " + availableCount);
        } else if (availableCount == 0 && totalCount > 0) {
            System.out.println("Sorry, no more copies of '" + bookTitle + "' are available at this moment");
        } else {
            System.out.println("The book '" + bookTitle + "' is not in our catalog.");
        }
    }

    // This will print the available books in the arraylist.
    // also modified this method to use the updated get count method
    public void printAvailableBooks() {
        // this will keep the title of the books so when program print out the left over available book counts for specific book title,
        ArrayList<String> used = new ArrayList<>();
        for (Book book : books) {
            String bookTitle = book.getTitle();
            // Get the available book counts
            int[] count = get_count(book.getTitle());
            int availableCount = count[0];
            // if the book title is not duplicate, get the book title and put it with the count
            if (!used.contains(bookTitle) && availableCount > 0) {
                used.add(bookTitle);
                System.out.println(bookTitle + ", remaining numbers of copies : " + availableCount);
            }
        }
        // if there is no book, there is no book in catalog
        if (used.isEmpty())
            System.out.println("No book in catalog");
    }

    public void printAddress() {
        System.out.println(ADDRESS);
    }

    // method to return the book to the library
    // it checks the book title and for the every book object in the arraylist, they compare the title and given book title and check if they are borrowed or not
    // if any of those books are borrowed, it allows the user to return the book since there is already book that has been borrowed
    // modified return book also
    public void returnBook(String bookTitle) {
        int[] counts = get_count(bookTitle);
        int availableCount = counts[0];

        if (availableCount > 0) {
            for (Book book : books) {
                if (book.getTitle().equals(bookTitle) && book.isBorrowed()) {
                    book.returned(book);
                    System.out.println("Returning " + bookTitle);
                    break;
                }
            }
            System.out.println("You successfully returned the book. The current copies of the book are : " + (availableCount + 1));
        } else {
            System.out.println("Book was not borrowed or is not in the catalog.");
        }
    }

    // From ChatGPT to import CSV file into the java file
    private List<String[]> readCSV(String filename) {
        List<String[]> data = new ArrayList<>();
        // reads each line of the CSV file using BufferReader
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // when they did not finish reading the line
            while ((line = br.readLine()) != null) {
                // Split the line into an array of values using a comma as the delimiter
                String[] values = line.split(",");
                data.add(values);
            }
            // catch the exceptions, and print the error if it happens
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void addBooksFromCSV(String fileDirectory) {
        // get the data from the method redCSV, and create list out of it
        List<String[]> csvData = readCSV(fileDirectory);

        for (String[] row : csvData) {
            if (row.length >= 2) {
                String bookTitle = row[0];
                // parse the integer
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
    
