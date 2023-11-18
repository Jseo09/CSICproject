public class Book {
    private String title;
    boolean borrowed;
    public Book(String title){
        this.title = title;
    }
    //Changes the book variable to it has been rented
    public void rent(){
        this.borrowed = true;
    }
    //Changes the book variable to it has been returned to the library
    //Renting and returning is for keeping the count of the books that is available in the specific library
    public void returned(){
        this.borrowed = false;
    }
    //checks if the book has been borrowed or not
    public boolean isBorrowed(){
        return this.borrowed;
    }
    //the method to get the title of the book
    public String getTitle(){
        return this.title;
    }
    public static void main(String[] args){
        Book example = new Book("The Da Vinci Code");
        System.out.println("Title : " + example.getTitle());
        System.out.println("Borrowed? : " + example.isBorrowed());
        System.out.println("Borrowed? (should be true): " + example.isBorrowed());
        System.out.println("Borrowed? (should be false): " + example.isBorrowed());
    }

}
