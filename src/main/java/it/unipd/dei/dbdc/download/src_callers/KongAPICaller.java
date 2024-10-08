package it.unipd.dei.dbdc.download.src_callers;

import it.unipd.dei.dbdc.download.interfaces.APICaller;
import it.unipd.dei.dbdc.tools.PathManager;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.File;
import java.util.Map;

/**
 * This class is an adapter which provides a simple way to call any API.
 * It implements the {@link APICaller} interface and uses the {@link Unirest} library.
 * It saves the downloaded file in the path specified.
 * At the end of the calls, the method {@link KongAPICaller#endRequests()} should be called to end the connection.
 *
 */
public class KongAPICaller implements APICaller {

    /**
     * The only constructor of this class.
     * It changes the cookies settings of {@link Unirest} to avoid errors or warnings during the calls.
     *
     */
    public KongAPICaller()
    {
        // This is needed as the cookies setting gives an error
        Unirest.config().cookieSpec("ignoreCookies");
    }

    /**
     * The main method of the class: it sends the request to the specified API with the specified params
     * and saves the result of the call as a file at the specified path. If the base url or the path are null,
     * or any exception occurs, returns false.
     * It is preferred to return a boolean and not throw any exception, as this simplifies the logic of
     * the program.
     *
     * @param base_url The base url of the API to call.
     * @param params The parameters of the call to the API.
     * @param path The path of the file where the response should be saved.
     * @return A boolean representing the success of the call, or false if any exception occurs.
     *
     * @see APICaller#sendRequest(String, Map, String)
     */
    @Override
    public boolean sendRequest(String base_url, Map<String, Object> params, String path) {
        if (base_url == null || path == null)
        {
            return false;
        }
        // To save the files in a path, we first have to make sure that there is
        // no other file with that name in that directory.
        PathManager.deleteDirOrFile(new File(path));
        try {
            HttpResponse<File> res = Unirest.get(base_url).queryString(params).asFile(path);
            return res.isSuccess();
        }
        //This is required as we don't know what are the customized exceptions that the library could throw
        //(they are not indicated in the javadocs)
        catch (RuntimeException e)
        {
            return false;
        }
    }

    /**
     * This method ends the connection with the server.
     * It should be called after the end of the requests to the server.
     * @see APICaller#endRequests()
     */
    @Override
    public void endRequests()
    {
        Unirest.shutDown();
    }

    /**
     * This function overrides the function of Object. It returns true if the Object passed
     * is of the same class of this object, as the two objects do not have any field.
     *
     * @param o The Object to compare to
     * @return True if the two objects are of the same class
     */
    @Override
    public boolean equals(Object o)
    {
        return (o instanceof KongAPICaller);
    }
}
