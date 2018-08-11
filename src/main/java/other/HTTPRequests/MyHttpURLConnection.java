package other.HTTPRequests;


import org.junit.Assert;
import java.io.*;
import java.net.*;

/**
 * класс для работы с HttpURLConnection
 * https://www.codejava.net/java-se/networking/how-to-use-java-urlconnection-and-httpurlconnection
 */
public class MyHttpURLConnection {
    /**
     * основная функция
     */
    public static void main(String [ ] args) throws Exception {
        MyHttpURLConnection myHttpURLConnection = new MyHttpURLConnection();
        myHttpURLConnection.funcAddCookie();

        URL url = new URL("http://htmlbook.ru/");

        /* Note that the openConnection() method doesn’t establish an actual network connection.
        It just returns an instance of URLConnection class.
        The network connection is made explicitly when the connect() method is invoked,
        or implicitly when reading header fields or getting an input stream / output stream.*/

        //----------
        HttpURLConnection urlConnection1 = (HttpURLConnection) url.openConnection();
        urlConnection1.setRequestProperty("User-Agent", "Mozilla/5.0");
        urlConnection1.setRequestMethod("GET");
        //urlConnection1.connect();
        System.out.println("urlConnection1.getContentType() =  " + urlConnection1.getContentType());
        int  responseCode = urlConnection1.getResponseCode();
        urlConnection1.disconnect();
        Assert.assertEquals(responseCode,HttpURLConnection.HTTP_OK );
        //----------
        HttpURLConnection urlConnection2 = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection2.getInputStream();
        System.out.println("urlConnection2.getResponseCode() =  " + urlConnection2.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while(reader.read()!=-1)
            System.out.println(reader.readLine());
        reader.close();
        urlConnection2.disconnect();
        //----------
        HttpURLConnection urlConnection3 = (HttpURLConnection) url.openConnection();
        urlConnection3.setRequestMethod("POST");
        urlConnection3.setDoOutput(true);
        OutputStreamWriter wr= new OutputStreamWriter(urlConnection3.getOutputStream());
        String str = "some string";
        wr.write(str);
        //urlConnection3.connect();
        System.out.println("urlConnection3.getResponseCode() =  " + urlConnection3.getResponseCode());
        wr.close();
        urlConnection3.disconnect();
    }

    private void funcAddCookie() throws Exception{
        URL url = new URL("http://htmlbook.ru/");
        HttpCookie cookie = new HttpCookie("lang", "fr");
        cookie.setDomain(url.toString());
        cookie.setPath("/");
        cookie.setVersion(0);
        CookieManager cookieManager = new CookieManager();
        cookieManager.getCookieStore().add(url.toURI(), cookie);
    }
}