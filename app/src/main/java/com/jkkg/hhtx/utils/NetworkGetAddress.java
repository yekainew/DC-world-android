package com.jkkg.hhtx.utils;

import android.location.Location;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

/**
 * Create by
 * on 2020/8/5
 * Email:oubaoyi@outlook.com
 */
public class NetworkGetAddress extends AsyncTask<Location, Void, String> {

    @Override
    protected String doInBackground(Location... p) {
        Location location=p[0];
        String baseurl = "http://api.map.baidu.com/geocoder?output=json&location="+location.getLatitude()+","+location.getLongitude()+"&ak=esNPFDwwsXWtsQfw4NMNmur1";
        Timber.e("================NetworkGetAddress url:"+baseurl);
        // 设置请求超时
        int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 30000;
        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(baseurl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setUseCaches(false);

            InputStream inputStream =  urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String b;
            while ((b = bufferedReader.readLine()) != null) {
                stringBuilder.append(b);
            }
            inputStream.close();
        } catch (IOException e) {
               e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                //释放资源
                urlConnection.disconnect();
            }
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String m_list) {
        super.onPostExecute(m_list);
        Timber.e("================NetworkGetAddress onPostExecute:"+m_list);
        if(mCallBack!=null){
            mCallBack.setLocation(m_list);
        }
    }

    private IGetLocationCallBack mCallBack;


    public NetworkGetAddress(IGetLocationCallBack mCallBack){
        super();
        this.mCallBack=mCallBack;
    }


    public static NetworkGetAddress getLocation(IGetLocationCallBack callBack,Location location){
        return (NetworkGetAddress) new NetworkGetAddress(callBack).execute(location);
    }

    public interface IGetLocationCallBack{
        void setLocation(String location);
    }
}
