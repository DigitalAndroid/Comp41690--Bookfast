package ie.bookfast.bookfast;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BookDetail extends AppCompatActivity {

    private static Book amazonBook;
    Button toAmazonBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String ISBN = getIntent().getStringExtra("ISBN");

        final Button favButton = (Button) findViewById(R.id.favButton);
        favButton.setVisibility(View.INVISIBLE);

        new FetchBookInfo().execute(ISBN);

        toAmazonBtn = (Button) findViewById(R.id.amazon_button);
        toAmazonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAmazon();
            }
        });
    }


    private class FetchBookInfo extends AsyncTask<String, Void, Book> {
        @Override
        protected Book doInBackground(String... strings) {
            try {
                GoogleBooksAPI mGoogleBooks = new GoogleBooksAPI();

                return mGoogleBooks.searchForISBN(strings[0]);

            }catch(Exception e){
                Log.e("FetchBookInfo", e.getMessage(), e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(final Book book) {
            TextView textViewDesc = (TextView) findViewById(R.id.textViewDesc);
            final Button favButton = (Button) findViewById(R.id.favButton);

            if(book != null) {
                amazonBook = book;
                Log.d("Book", book.toString());
                // title
                TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
                textViewTitle.setText(book.title);

                // author
                TextView textViewAuthors = (TextView) findViewById(R.id.textViewAuthors);
                textViewAuthors.setText(book.authorsString);

                // description
                textViewDesc.setText(book.description+"\n\n\n");
                

                // load image
                ImageView bookCover = (ImageView) findViewById(R.id.imageViewBookCover);
                Picasso.with(getApplicationContext()).load(book.thumbnailURL).into(bookCover);

                // button
                final DBConnection dbConnection = new DBConnection(getApplicationContext());
                favButton.setVisibility(View.VISIBLE);

                if(dbConnection.isBookInFavourites(book.isbn)){
                    favButton.setText("Remove from Favourites");
                }else{
                    favButton.setText("Add to Favourites");
                }
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dbConnection.isBookInFavourites(book.isbn)) {
                            dbConnection.removeFavBook(book.isbn);
                            favButton.setText("Add to Favourites");
                            Toast toast = Toast.makeText(getApplicationContext(), book.title + " removed from your Favourites", Toast.LENGTH_LONG);
                            toast.show();
                        }else{
                            dbConnection.addFavBook(book);
                            favButton.setText("Remove from Favourites");
                            Toast toast = Toast.makeText(getApplicationContext(), book.title + " added to your Favourites", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        Log.d("All Books", dbConnection.getAllFavBooks().toString());
                    }
                });

            }else {
                textViewDesc.setText("No books were found :'(");
                textViewDesc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                favButton.setVisibility(View.GONE);
            }
        }
    }

    private void toAmazon() {
        Intent amazonIntent = new Intent(this, AmazonStoreActivity.class);
        String argument = amazonBook.title + " " + amazonBook.authorsString;
        amazonIntent.putExtra("book_data", argument);
        startActivity(amazonIntent);
    }


}
