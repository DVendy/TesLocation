package com.kreasys.dvendy.teslocation;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedInputStream;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Iterator;
        import java.util.List;

/**
 * Created by DVendy - Kreasys on 5/28/2015.
 */
public class ServerDatabaseHandler {
    private String baseUrl;
    final String arrJSONKey = "tes_map";

    public ServerDatabaseHandler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isServerReachable(String baseUrl) {
        try {
            URL urlServer = new URL(baseUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                return true;
            } else {
                return false;
            }
        }catch (IOException e) {
            return false;
        }
    }

    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        try {
            URL url2 = new URL(url);
            str = new StringBuilder();

            //HttpResponse response = client.execute(httpGet);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return str.toString();
    }

    public String getJSONUrl(String url, String param) {
        StringBuilder str = new StringBuilder();
        OutputStreamWriter request;
        try {
            URL url2 = new URL(url);

            //HttpResponse response = client.execute(httpGet);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("GET");

            request = new OutputStreamWriter(conn.getOutputStream());
            request.write(param);
            request.flush();
            request.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        //System.out.println(str.toString());
        return str.toString();
    }

    public boolean setLocation(String user_id, String date, String latitude, String longitude, String status, String keterangan) throws JSONException{
        String param = "id="+user_id+"&date="+date+"&latitude="+latitude+"&longitude="+longitude+"&status="+status+"&keterangan="+keterangan;
        String jsonString = getJSONUrl(baseUrl + "/MobileMap/setLocation.php",param);
        System.out.println(jsonString);
        if (jsonString.trim().equals("success"))
            return true;
        else
            return false;
    }

    public boolean login(String username, String password){
        String param = "username="+username+"&password="+password;
        String jsonString = getJSONUrl(baseUrl + "/MobileMap/login.php",param);
        if (jsonString.trim().equals("success")){
            return true;
        }
        else
            return false;
    }

    public String getVersion() throws JSONException {
        String jsonString = getJSONUrl(baseUrl + "/TesDatabase/version.php");

        if(jsonString!=null) {
            JSONObject data = new JSONObject(jsonString);
            JSONArray Jarray = data.getJSONArray(arrJSONKey);

            String version = "";
            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject c = Jarray.getJSONObject(i);
                version = c.getString("date");
            }
            return version;
        }
        else
        {
            return null;
        }
    }

}
