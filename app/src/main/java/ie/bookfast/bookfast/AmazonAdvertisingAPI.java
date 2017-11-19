package ie.bookfast.bookfast;

import de.codecrafters.apaarb.AmazonProductAdvertisingApiRequestBuilder;
import de.codecrafters.apaarb.AmazonWebServiceAuthentication;
import de.codecrafters.apaarb.AmazonWebServiceLocation;

import static de.codecrafters.apaarb.ItemCategory.BOOKS;
import static de.codecrafters.apaarb.ItemInformation.ATTRIBUTES;
import static de.codecrafters.apaarb.ItemInformation.IMAGES;
import static de.codecrafters.apaarb.ItemInformation.OFFERS;
import static de.codecrafters.apaarb.ItemInformation.REVIEWS;

//Created by Luke on the 09/11/17

public class AmazonAdvertisingAPI {

    //Access Key ID
    private static final String ACCESS_KEY_ID = "AKIAJI54MHWKACQJNFPQ";

    //Secret Key corresponding to the above ID
    private static final String SECRET_KEY = "ToqbiLgmuaGoTc5kFYGxYc5vdWzKG8gHR9AxWcCa";

    //Our associate tag
    private static final String ASSOCIATE_TAG = "bookfast0a-21";

    private String requestURL = null;

    public AmazonAdvertisingAPI(){}

    public String searchForBook(String bookSearchDetails) {

        AmazonWebServiceAuthentication myAuthentication
                = AmazonWebServiceAuthentication.create(ASSOCIATE_TAG, ACCESS_KEY_ID, SECRET_KEY);

        requestURL = AmazonProductAdvertisingApiRequestBuilder
                .forItemSearch(bookSearchDetails)
                .includeInformationAbout(ATTRIBUTES)
                .includeInformationAbout(OFFERS)
                .includeInformationAbout(IMAGES)
                .includeInformationAbout(REVIEWS)
                .filterByCategroy(BOOKS)
                .createRequestUrlFor(AmazonWebServiceLocation.CO_UK, myAuthentication);

        return requestURL;
    }
}