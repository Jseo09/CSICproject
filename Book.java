public class Book {
    private String title;
    boolean borrowed;
    public Book(String title){
        this.title = title;
    }
    public void rented(Book books){
        books.borrowed = true;
    }

    public void returned(Book books){
        books.borrowed = false;
    }
    public boolean isBorrowed(){
        return this.borrowed;
    }
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
