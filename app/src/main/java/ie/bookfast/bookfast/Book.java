package ie.bookfast.bookfast;


import java.util.ArrayList;

/**
 * Created by Cathal on 06/11/2017.
 */

public class Book {
    public String isbn;
    public String title;
    public ArrayList<String> authors;
    public String authorsString;
    public String description;
    public String publisher;
    public String publishedDate;
    public String thumbnailURL;
    public String category;


    // api constructor
    public Book(
            String isbn,
            String title,
            ArrayList<String> authors,
            String description,
            String publisher,
            String publishedDate,
            String thumbnailURL,
            String category){
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.authorsString = generateAuthorsString(authors);
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.thumbnailURL = thumbnailURL;
        this.category = category;
    }

    // db constructor
    public Book(
            String isbn,
            String title,
            String authorsString,
            String description,
            String thumbnailURL,
            String category){
        this.isbn = isbn;
        this.title = title;
        this.authorsString = authorsString;
        this.description = description;
        this.thumbnailURL = thumbnailURL;
        this.category = category;
    }

    private String generateAuthorsString(ArrayList<String> authors) {
        int length = authors.size();
        String authorsString = "";

        for(int i=0; i<length; i++){
            String author = authors.get(i);
            if(authorsString.isEmpty()) authorsString = author;
            else if (i == length-1) authorsString += " and " + author;
            else {
                authorsString += ", " + author;
            }
        }

        return authorsString;
    }

}
