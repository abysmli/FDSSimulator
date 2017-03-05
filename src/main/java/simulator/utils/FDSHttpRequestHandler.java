package simulator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

/**
 * class HttpRequestHandler Handler Center for all post requests Get all request
 * from Java GUI and send them to backend Get Post Responds from backend and
 * forward to Java GUI
 *
 * @author Li, Yuan
 */
public class FDSHttpRequestHandler {

    private final HttpClient client = HttpClientBuilder.create().build();
    private final CookieStore cookieStore = new BasicCookieStore();
    private final HttpContext httpContext = new BasicHttpContext();
    private final String USER_AGENT = "Mozilla/5.0";
    private final String sURL;

    public FDSHttpRequestHandler(String URL) {
        sURL = URL;
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    public JSONObject postRuntimeData(JSONObject sendData) throws Exception {
        String url = "http://" + sURL + "/postRuntimeData";
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("component_data", sendData.toString()));
        StringBuilder content = sendPostRequest(url, urlParameters);
        JSONObject result = new JSONObject(content.toString());
        return result;
    }

    public JSONObject reportFault(JSONObject faultData) throws Exception {
        String url = "http://" + sURL + "/reportFault";
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("fault_data", faultData.toString()));
        StringBuilder content = sendPostRequest(url, urlParameters);
        JSONObject result = new JSONObject(content.toString());
        return result;
    }

    public JSONObject connectionStatus() throws Exception {
        String url = "http://" + sURL + "/status";
        List<NameValuePair> urlParameters = new ArrayList<>();
        StringBuilder content = sendGetRequest(url, urlParameters);
        JSONObject result = new JSONObject(content.toString());
        return result;
    }

    public JSONObject updateStatus(JSONObject updateMeta) throws Exception {
        String url = "http://" + sURL + "/updateStatus";
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("updateMeta", updateMeta.toString()));
        StringBuilder content = sendPostRequest(url, urlParameters);
        JSONObject result = new JSONObject(content.toString());
        return result;
    }

    private StringBuilder sendGetRequest(String url, List<NameValuePair> urlParameters)
            throws IOException, UnsupportedEncodingException, IllegalStateException {

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", USER_AGENT);
        try {
            HttpResponse response = client.execute(httpGet, httpContext);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = rd.readLine())) {
                content.append(line);
            }
            return content;
        } catch (ConnectException e) {
            System.out.println(e);
            return new StringBuilder().append("{'status':'close'}");
        }
    }

    private StringBuilder sendPostRequest(String url, List<NameValuePair> urlParameters)
            throws IOException, UnsupportedEncodingException, IllegalStateException {

        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        try {
            HttpResponse response = client.execute(post, httpContext);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder content = new StringBuilder();
            // convert responded data to StringBuilder
            String line;
            while (null != (line = rd.readLine())) {
                content.append(line);
            }
            return content;
        } catch (ConnectException e) {
            System.out.println(e);
            return new StringBuilder().append("{}");
        }
    }
}
