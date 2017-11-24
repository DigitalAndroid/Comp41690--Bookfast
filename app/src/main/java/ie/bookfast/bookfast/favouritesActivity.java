package ie.bookfast.bookfast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class favouritesActivity extends AppCompatActivity {
    String[] titles, authors, publishers, ISBN;
    DBConnection dbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ListView favouritesListView = (ListView) findViewById(R.id.favouritesListView);

        //Below populates title and author string lists
        //connecting to DB
        dbConnection = new DBConnection(this);

        //populating list with
        List<Book> favouriteBookList = dbConnection.getAllFavBooks();

        //below i am populating lists with book data (title and author)
        titles = new String[favouriteBookList.size()];

        for (int i=0; i<favouriteBookList.size(); i++){
            titles[i] = favouriteBookList.get(i).title;
        }

        authors = new String[favouriteBookList.size()];

        for (int i=0; i<favouriteBookList.size(); i++){
            authors[i] = favouriteBookList.get(i).authorsString;
        }

        publishers = new String[favouriteBookList.size()];

        for (int i=0; i<favouriteBookList.size(); i++){
            publishers[i] = favouriteBookList.get(i).publisher;
        }

        ISBN = new String[favouriteBookList.size()];

        for (int i=0; i<favouriteBookList.size(); i++){
            ISBN[i] = favouriteBookList.get(i).isbn;
        }

        CustomAdapter customAdapter = new CustomAdapter();
        favouritesListView.setAdapter(customAdapter);

    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.favourite_list_layout, null);

            final int index = i;

            TextView titleTV = (TextView) view.findViewById(R.id.favouriteTitle);
            TextView authorTV = (TextView) view.findViewById(R.id.favouriteAuthor);
            TextView publisherTV = (TextView) view.findViewById(R.id.favouritePublisher);
            Button deleteFavButton = (Button) view.findViewById(R.id.deleteFavButton);

            titleTV.setText(titles[index]);
            authorTV.setText(authors[index]);
            publisherTV.setText(publishers[index]);
            deleteFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbConnection.removeFavBook(ISBN[index]);
                }
            });
            return view;
        }
    }
}
