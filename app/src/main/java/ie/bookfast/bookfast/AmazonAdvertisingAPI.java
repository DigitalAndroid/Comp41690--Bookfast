package ie.bookfast.bookfast;

import java.util.*;

//Created by Luke on the 09/11/17

public class AmazonAdvertisingAPI {

    //Access Key ID
    private static final String ACCESS_KEY_ID = "AKIAJI54MHWKACQJNFPQ";

    //Secret Key corresponding to the above ID
    private static final String SECRET_KEY = "ToqbiLgmuaGoTc5kFYGxYc5vdWzKG8gHR9AxWcCa";

    //Our associate tag
    private static final String ASSOCIATE_TAG = "bookfast0a-21";

    //Service we are using
    private static final String SERVICE = "AWSECommerceService";

    //The region we are linking to
    private static final String ENDPOINT = "webservices.amazon.co.uk";

    private String requestURL = null;
    SignedRequestsHelper helper;

    public AmazonAdvertisingAPI(){
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public String searchForBook(String bookSearchDetails) {

        final Map<String, String> params = new LinkedHashMap<>();
        params.put("Service", SERVICE);
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", ACCESS_KEY_ID);
        params.put("AssociateTag", ASSOCIATE_TAG);
        params.put("SearchIndex", "Books");
        params.put("ResponseGroup", "Images,ItemAttributes,OfferFull,Reviews");
        params.put("Keywords", bookSearchDetails);

        requestURL = helper.sign(params);

        return requestURL;
    }
}