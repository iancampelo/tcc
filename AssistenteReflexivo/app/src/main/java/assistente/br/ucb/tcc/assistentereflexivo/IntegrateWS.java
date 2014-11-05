package assistente.br.ucb.tcc.assistentereflexivo;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ian.campelo on 10/23/14.
 */
public class IntegrateWS {

    private ArrayList<NameValuePair> params;
    private ArrayList <NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public IntegrateWS(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception
    {
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(NameValuePair p : params)
                    {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF - 8");
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }

                HttpGet request = new HttpGet(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
//                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    NameValuePair nv = params.get(0);
                    BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
                    basicHttpEntity.setContent(new ByteArrayInputStream(nv.getValue().getBytes(StandardCharsets.UTF_8)));
                    request.setEntity(basicHttpEntity);
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();

        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static  synchronized String invokeWS(User user, final Context mContext){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        final String[] res = {new String()};

        Looper.prepare();
        try {
            //TODO this thing work?
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams rp = new RequestParams();
            rp.add("content", user.toJson());
            URL url = new URL("http://192.168.1.4:8080/webservice/usuario/consultarUsuario");
            HeaderGroup hg = new HeaderGroup();
            hg.addHeader(new BasicHeader("Accept", "application/json"));
            hg.addHeader(new BasicHeader("Content-type", "application/json"));
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(user.toJson().getBytes(StandardCharsets.UTF_8)));

            client.post(mContext, url.toString(), hg.getAllHeaders(), basicHttpEntity, "application/json", new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    // success logic here
                    res[0] = response;
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String error, Throwable e) {
                    // handle failure here
                    res[0] = error;
                }
            });
            client.notifyAll();
            return res[0];
        }

//                @Override
//                public void onSuccess(int i, Header[] headers, String s, Object o) {
//                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onFailure(int i, Header[] headers, Throwable throwable, String s, Object o) {
//                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                protected Object parseResponse(String s, boolean b) throws Throwable {
//                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
//
//                    return null;
//                }
//            });
            // When the response returned by REST has Http response code '200'

            //TODO ASYNHANDLER
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
//                    String response = new String(bytes);
//
//                    try {
//                        // JSON Object
//                        JSONObject obj = new JSONObject(response);
//                        User user1 = new Gson().fromJson(response,User.class);
//
////                        User newUser = new Gson().fromJson(response,User.class);
////
////                        Toast.makeText(mContext, newUser.toString(), Toast.LENGTH_LONG).show();
//                        res[0] = response;
//
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        Toast.makeText(mContext, "Error Occured [Server's JSON response " +
//                                "might be invalid]!", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                        res[0] = "erro";
//
//                    }catch (Exception e){
//                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
//                        res[0] = "erro";
//                    }
//
//                }
//                // When the response returned by REST has Http response code other than '200'
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
//
//                    // When Http response code is '404'
//                    if (statusCode == 404) {
//                        Toast.makeText(mContext, "Requested resource not found", Toast.LENGTH_LONG).show();
//                    }
//                    // When Http response code is '500'
//                    else if (statusCode == 500) {
//                        Toast.makeText(mContext, "Something went wrong at server end", Toast.LENGTH_LONG).show();
//                    }
//                    // When Http response code other than 404, 500
//                    else {
//                        Toast.makeText(mContext, "Unexpected Error occcured! [Most common Error: Device might not be connected " +
//                                "to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
//                    }
//                    res[0] = "erro";
//                }
        catch (Exception e){
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ERRO_ASYNC_HTTP", e.getMessage());
            res[0] = "erro";
            return res[0];
        }
    }
//TODO implementar login com o WS - ver questão de esperar a resposta do servidor.
//    public static String loginWS(User user, final Context mContext){
//        // Show Progress Dialog
//        // Make RESTful webservice call using AsyncHttpClient object
//        final String[] res = {new String()};
//
//        try{
//            //TODO this thing work?
//            SyncHttpClient client = new SyncHttpClient();
//            RequestParams rp = new RequestParams();
//            rp.add("content",user.toJson());
//            URL url = new URL("http://192.168.1.4:8080/webservice/usuario/consultarUsuario");
//            HeaderGroup hg = new HeaderGroup();
//            hg.addHeader(new BasicHeader("Accept", "application/json"));
//            hg.addHeader(new BasicHeader("Content-type", "application/json"));
//            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
//            basicHttpEntity.setContent(new ByteArrayInputStream(user.toJson().getBytes(StandardCharsets.UTF_8)));
//            client.post(mContext,url.toString(),hg.getAllHeaders(),basicHttpEntity,"application/json", new AsyncHttpResponseHandler() {
//                // When the response returned by REST has Http response code '200'
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
//                    String response = new String(bytes);
//
//                    try {
//                        // JSON Object
//                        JSONObject obj = new JSONObject(response);
//                        User user1 = new Gson().fromJson(response,User.class);
//
////                        User newUser = new Gson().fromJson(response,User.class);
////
////                        Toast.makeText(mContext, newUser.toString(), Toast.LENGTH_LONG).show();
//                        res[0] = response;
//
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        Toast.makeText(mContext, "Error Occured [Server's JSON response " +
//                                "might be invalid]!", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                        res[0] = "erro";
//
//                    }catch (Exception e){
//                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
//                        res[0] = "erro";
//                    }
//
//                }
//                // When the response returned by REST has Http response code other than '200'
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
//
//                    // When Http response code is '404'
//                    if (statusCode == 404) {
//                        Toast.makeText(mContext, "Requested resource not found", Toast.LENGTH_LONG).show();
//                    }
//                    // When Http response code is '500'
//                    else if (statusCode == 500) {
//                        Toast.makeText(mContext, "Something went wrong at server end", Toast.LENGTH_LONG).show();
//                    }
//                    // When Http response code other than 404, 500
//                    else {
//                        Toast.makeText(mContext, "Unexpected Error occcured! [Most common Error: Device might not be connected " +
//                                "to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
//                    }
//                    res[0] = "erro";
//                }
//            });
//        }catch (Exception e){
//            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
//            Log.e("ERRO_ASYNC_HTTP", e.getMessage());
//            res[0] = "erro";
//        }
//        finally {
//            return res[0];
//        }
//    }


}
