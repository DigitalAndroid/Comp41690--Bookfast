package ie.bookfast.bookfast;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String ISBN = getIntent().getStringExtra("ISBN");

        new FetchBookInfo().execute(ISBN);
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
        protected void onPostExecute(Book book) {
            if(book != null) {
                Log.d("Book", book.toString());
                // title
                TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
                textViewTitle.setText(book.title);

                // author
                TextView textViewAuthors = (TextView) findViewById(R.id.textViewAuthors);
                textViewAuthors.setText(book.authorsString);

                // author
                TextView textViewDesc = (TextView) findViewById(R.id.textViewDesc);
                textViewDesc.setText(book.description+"\n\n\n");

                // load image
                ImageView bookCover = (ImageView) findViewById(R.id.imageViewBookCover);
                Picasso.with(getApplicationContext()).load(book.thumbnailURL).into(bookCover);
            }else {
                TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
                textViewTitle.setText("Something went horribly wrong");
            }
        }
    }




}
