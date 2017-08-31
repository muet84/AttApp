package com.ibrahim.mibrahim.attapp;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

/**
 * Created by M.Ibrahim on 4/9/2017.
 */

public class SyncData {

    Context context;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    int smsPagination =0;

    public SyncData(Context context){
        this.context = context;
        sharedPrefrencesHelper =SharedPrefrencesHelper.getInstance(context);



    }

    public ArrayList<SmsBean> syncMessages(final SmsActivity.onMessagesComplete onMessagesCompleteCallback){

        int smsPage = Integer.parseInt(sharedPrefrencesHelper.getValue("smsPageTotal","0"));

            AsyncHttpClient client = new AsyncHttpClient();
            final ArrayList<SmsBean> smsList = new ArrayList<>();
            String deviceKey = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID, "default");
            final String URL = DATA.URL_SMS + deviceKey;
            //public static String URL_SMS = "http://schoolapi.kidsroboticacademy.com/api/v1/notification?device_key=15de21c670ae7c3f6f3f1f37029303c9";
            client.setTimeout(900000000);
            client.get(URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    if (headers != null) {

                        HashMap<String, String> headerMap = convertHeadersToHashMap(headers);
                        String smsPageTotal = headerMap.get("X-Pagination-Page-Count");
                        Log.i("pageTotal", smsPageTotal);
                        sharedPrefrencesHelper.setValue("smsPageTotal", smsPageTotal);

                    }


                    String response = new String(responseBody);
                    // Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                    Log.i("response", "" + URL + "\n" + response);

                    try {
                        JSONArray smsArray = new JSONArray(response);
                        for (int a = 0; a < smsArray.length(); a++) {

                            JSONObject smsObject = smsArray.getJSONObject(a);

                            String server_id = smsObject.getString("id");
                            String school_id = smsObject.getString("school_id");
                            String parent_id = smsObject.getString("parent_id");
                            String parent_type = smsObject.getString("parent_type");
                            String type = smsObject.getString("type");
                            String medium = smsObject.getString("medium");
                            String to_number = smsObject.getString("to_number");
                            String from_text = smsObject.getString("from_text");
                            String title = smsObject.getString("title");
                            String contents = smsObject.getString("contents");
                            String status = smsObject.getString("status");
                            String created_by = smsObject.getString("created_by");
                            String updated_by = smsObject.getString("updated_by");
                            String created_at = smsObject.getString("created_at");
                            String updated_at = smsObject.getString("updated_at");

                            smsList.add(new SmsBean("", server_id, school_id, parent_id, parent_type, type, medium,
                                    to_number, from_text, title, contents, status, created_by, updated_by, created_at, updated_at));

                        }
                        onMessagesCompleteCallback.onSuccess(smsList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        onMessagesCompleteCallback.onFailue(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                  if(responseBody!=null){
                      Log.i("onFailureResponseBody",new String(responseBody));
                  }

                    Log.i("onFailure",statusCode +"\n" + headers +"\n" + error.getMessage() + "\n" + error.getCause());
                    onMessagesCompleteCallback.onFailue(error);


                }
            });



        return null;
    }

    private HashMap<String, String> convertHeadersToHashMap(Header[] headers) {
        HashMap<String, String> result = new HashMap<String, String>(headers.length);
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }
        return result;
    }

}
