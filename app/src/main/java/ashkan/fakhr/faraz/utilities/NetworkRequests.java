package ashkan.fakhr.faraz.utilities;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

import static ashkan.fakhr.faraz.utilities.Constants.DEBUG;
import static ashkan.fakhr.faraz.utilities.Constants.LOG_TAG;
import static ashkan.fakhr.faraz.utilities.Snippets.isOnline;

/**
 * Created by Amin on 20/05/2016.
 */
public class NetworkRequests {

    public static void getRequest(final String url, final Interfaces.NetworkListeners listener, final String tag) {
        if (DEBUG) {
            Log.d(LOG_TAG, " url = " + url);
        }

        // creating volley string request
        final StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (DEBUG) {
                    Log.d(LOG_TAG, " response for url " + url + " ===== " + response);
                }
                listener.onResponse(response, tag);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // something went wrong
                listener.onError(error, tag);
            }
        });


        if (isOnline(AppController.applicationContext)) {
            AppController.getInstance().addToRequestQueue(strReq, "request");
        } else {
            listener.onOffline(tag);
        }

    }

    public static void postRequest(final String url, final Interfaces.NetworkListeners listener,
                                   final String tag, final String jsonData) {


        if (DEBUG) {
            Log.d(LOG_TAG, tag + " url = " + url);
            Log.d(LOG_TAG, " post data for url " + url + jsonData);
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (!(response.contains("ا") || response.contains("ب") || response.contains("پ") ||
                        response.contains("ت") || response.contains("ث") || response.contains("ج")
                        || response.contains("چ") || response.contains("ح") || response.contains("خ")
                        || response.contains("د") || response.contains("ذ") || response.contains("ر")
                        || response.contains("ز") || response.contains("ژ") || response.contains("س")
                        || response.contains("ش") || response.contains("ص") || response.contains("ض")
                        || response.contains("ط") || response.contains("ظ") || response.contains("ع")
                        || response.contains("غ") || response.contains("ف") || response.contains("ق")
                        || response.contains("ک") || response.contains("گ") || response.contains("ل")
                        || response.contains("م") || response.contains("ن") || response.contains("و")
                        || response.contains("ه") || response.contains("ی"))) {
                    try {
                        response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (DEBUG) {
                    Log.d(LOG_TAG, " response for url " + url + " ===== " + response);
                }
                if (listener != null) {
                    listener.onResponse(response, tag);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (DEBUG) {
                    if (error.networkResponse != null) {
                        Log.d(LOG_TAG, " error for url " + url + " ===== " + error.networkResponse.statusCode);
                    } else {
                        Log.d(LOG_TAG, " error for url " + url);
                    }

                }
                if (listener != null) {
                    listener.onError(error, tag);
                }
            }
        }) {

            public String getBodyContentType() {
                return "application/json; charset=" + getParamsEncoding();
            }

            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonData.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

        };
        if (isOnline(AppController.applicationContext)) {
            AppController.getInstance().addToRequestQueue(strReq, "request");
        } else {

            if (listener != null) {
                listener.onOffline(tag);
            }
        }

    }
}
