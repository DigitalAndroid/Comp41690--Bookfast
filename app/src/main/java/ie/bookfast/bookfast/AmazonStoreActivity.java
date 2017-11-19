package ie.bookfast.bookfast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class AmazonStoreActivity extends AppCompatActivity implements AsyncResponse{

    private WebView mWebview;
    DownloadXmlTask asyncTask = new DownloadXmlTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amazon_store);

        String bookDetails = getIntent().getStringExtra("book_data");
        AmazonAdvertisingAPI myAmazon = new AmazonAdvertisingAPI();
        String URL = myAmazon.searchForBook(bookDetails);

        asyncTask.delegate = this;
        asyncTask.execute(URL);
    }

    @Override
    public void processFinish(String output){
        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.loadUrl(output);
    }
}