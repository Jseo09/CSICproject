import java.util.ArrayList;
import java.util.List;
import java.io.*;
public class Library {
    private final String ADDRESS;
    private ArrayList<Book> books = new ArrayList<>();



    public Library(String address) {
        this.ADDRESS = address;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    private int get_count(String bookTitle) {
        int count = 0;
        for (Book book : books) {
            if (book.getTitle().equals(bookTitle) && !book.isBorrowed()) {
                count++;
            }
        }
        return count;
    }

    public void borrowBook(String bookTitle) {
        boolean borrowed = false;
        boolean find = false;
        int count = get_count(bookTitle);
        for (Book book : books) {
            if (book.getTitle().equals(bookTitle) && !book.isBorrowed()) {
                find = true;
                book.rent(book);
                count--;
                break;
            }
        }
        if (find)
            System.out.println("You successfully borrowed " + bookTitle + ", remaining copies are " + count);
        else {
            for (Book book : books) {
                if (book.getTitle().equals(bookTitle)) {
                    borrowed = book.isBorrowed();
                    if(borrowed){
                        break; // This ensures the borrowed variable reflects the correct status
                    }
                }
            }
            if (borrowed)
                System.out.println("Sorry, this book is already borrowed.");
            else {
                System.out.println("The book you attempted to borrow is not in our catalog.");
            }
        }
    }

    public void printAvailableBooks() {
        ArrayList<String> used = new ArrayList<>();
        for (Book book : this.books) {
            int count = get_count(book.getTitle());
            if (!used.contains(book.getTitle())) {
                used.add(book.getTitle());
                System.out.println(book.getTitle() + ", remaining numbers of copies : " + count);
            }
        }
        if (used.isEmpty())
            System.out.println("No book in catalog");
    }

    public void printAddress() {
        System.out.println(ADDRESS);
    }

    public void returnBook(String bookTitle) {
        boolean isReturned = false;
        for (Book book : books) {
            if (book.getTitle().equals(bookTitle) && book.isBorrowed()) {
                book.returned(book);
                isReturned = true;
                System.out.println("Returning " + bookTitle);
                break;
            }
        }
        if (isReturned) {
            int currentCount = get_count(bookTitle); // Get the updated count after returning
            System.out.println("You successfully returned the book. The current copies of the books are : " + currentCount);
        } else {
            System.out.println("Book was not borrowed or is not in the catalog.");
        }

    }

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

    public void addBooksFromCSV(String fileDirectory) {
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
        Library thirdLibrary = new Library("12 Broadway St.");

        // Add four books to the first library
        firstLibrary.addBook(new Book("The Da Vinci Code"));
        firstLibrary.addBook(new Book("The Da Vinci Code")); // second copy
        firstLibrary.addBook(new Book("Le Petit Prince"));
        firstLibrary.addBook(new Book("A Tale of Two Cities"));
        firstLibrary.addBook(new Book("The Lord of the Rings"));
        firstLibrary.addBook(new Book("The Lord of the Rings")); // second copy
        //!!!!IMPORTANT!!!!
        //Change this directory since the file directory depends on csv file locations*
        thirdLibrary.addBooksFromCSV("C:/Users/seoji/IdeaProjects/School/src/catalog.csv/");

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
