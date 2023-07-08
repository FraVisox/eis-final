package it.unipd.dei.dbdc.download.src_api_managers.TheGuardianAPI;

import it.unipd.dei.dbdc.download.QueryParam;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that tests {@link GuardianAPIInfo}.
 */
@Order(7)
public class GuardianAPIInfoTest {

    /**
     * The default URL of the TheGuardianAPI.
     */
    private final static String defaultURL = "https://content.guardianapis.com/search?";

    /**
     * The possible fields of the TheGuardianAPI.
     */
    private final static QueryParam[] possible_fields = {
            new QueryParam("api-key", "MANDATORY: the key to access the API"),
            new QueryParam("page-size","The number of articles to have in a single file .json. Values: 1-200. Default = 200"),
            new QueryParam("pages", "The number of pages to download from the API. Default is 5, which means that by default are downloaded 1000 articles"),
            new QueryParam("q", "The topic of the articles to analysis for. Default is \"nuclear power\""),
            new QueryParam("order-by","The way the articles should be ordered (we take the first n in that order). Default = relevance"),
            new QueryParam("from-date", "The date to search from, in the format yyyy-mm-dd"),
            new QueryParam("to-date", "The date to search to, in the format yyyy-mm-dd")
    };

    /**
     * Tests of {@link GuardianAPIInfo#getFormattedParams()}. It uses reflection to access the
     * length of the spaces to be put after the key.
     */
    @Test
    public void getFormattedParams()
    {
        //Access the private static field and save it as a variable max_length
        Field length = null;
        try {
            length = GuardianAPIInfo.class.getDeclaredField("formatted_key_length");
        } catch (NoSuchFieldException e) {
            fail("Error in the reflection");
        }
        length.setAccessible(true);
        int max_length = 0;
        try {
            max_length = (Integer) length.get(null);
        } catch (IllegalAccessException e) {
            fail("Error in the reflection");
        }
        
        //Create the String in the same way it should be done inside the class
        StringBuilder par = new StringBuilder();
        for (QueryParam q : possible_fields)
        {
            StringBuilder this_field = new StringBuilder(q.getKey());
            while (this_field.length() < max_length)
            {
                this_field.append(" ");
            }
            this_field.append(q.getValue()).append("\n");
            par.append(this_field);
        }
        String params = par.toString();

        //Assertion
        assertEquals(params, GuardianAPIInfo.getFormattedParams());

        //End reflection
        length.setAccessible(false);
    }

    /**
     * Tests of {@link GuardianAPIInfo#getDefaultURL()}
     */
    @Test
    public void getDefaultURL()
    {
        assertEquals(defaultURL, GuardianAPIInfo.getDefaultURL());
    }

    /**
     * Tests of {@link GuardianAPIInfo#isPresent(String)} with valid and invalid inputs.
     */
    @Test
    public void isPresent()
    {
        for (QueryParam q : possible_fields) {
            assertTrue(GuardianAPIInfo.isPresent(q.getKey()));
        }
        assertFalse(GuardianAPIInfo.isPresent("invalid"));
        assertFalse(GuardianAPIInfo.isPresent("apikey"));
        assertFalse(GuardianAPIInfo.isPresent("page"));
        assertFalse(GuardianAPIInfo.isPresent(null));
        assertFalse(GuardianAPIInfo.isPresent(""));
    }

    /**
     * The only constructor of the class. It is declared as private to
     * prevent the default constructor to be created.
     */
    private GuardianAPIInfoTest() {}
}
