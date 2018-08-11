package other.HTTPRequests;

import com.google.gson.*;


import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * класс для работы с HttpsURLConnection
 * https://docs.oracle.com/javase/7/docs/api/javax/net/ssl/HttpsURLConnection.html
 * https://www.programcreek.com/java-api-examples/javax.net.ssl.HttpsURLConnection
 *
 */
public class MyHttpsURLConnection {

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs,
                                                   String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs,
                                                   String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }


    /**
     * основная функция
     */
    public static void main(String [ ] args) throws Exception {
        URL url = new URL("https://www.google.com.ua");
        HttpsURLConnection httpsConnection = (HttpsURLConnection) url.openConnection();
        httpsConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpsConnection.setRequestMethod("GET");
        System.out.println("httpsConnection.getResponseCode() = " + httpsConnection.getResponseCode());
        //-------------
        try{
            Certificate[] certs = httpsConnection.getServerCertificates();
            for(Certificate cert : certs){
                System.out.println("Cert Type : " + cert.getType());
                System.out.println("Cert Hash Code : " + cert.hashCode());
                System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
                System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
                System.out.println("\n");
            }
        } catch(SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }
        //--------------------
        try {
            System.out.println("****** Content of the URL ********");
            BufferedReader br = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
            String input;
            while ((input = br.readLine()) != null){
                System.out.println(input);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpsConnection.disconnect();
    }

    private void func1(String urlStr, String parameters) throws IOException {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);

            OutputStream output = conn.getOutputStream();
            output.write(parameters.getBytes("utf-8"));
            output.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            System.out.println("func1: " + sb.toString());
            reader.close();
    }
}

