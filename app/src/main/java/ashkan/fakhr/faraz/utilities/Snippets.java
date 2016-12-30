package ashkan.fakhr.faraz.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ashkan.fakhr.faraz.R;

/**
 * Created by Amin on 3/29/2015.
 * this class is place to save all those methods that are
 * going to be used multiple time in application and in different activities
 */
public class Snippets {

    //md5 converter
    public static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
this function is used to download an file from URL
and save it to the base location of app, with a folder name taken from string.xml
*/
    public static void downloadFile(String URL, String name, Context c) {

        File direct = new File(Environment.getExternalStorageDirectory()
                + c.getResources().getString(R.string.app_name));
        //check if the folder already exists
        Boolean b = direct.exists();

        if (!b) {
            //if not, try to create it
            b = direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true).setTitle(name + " " + URL)
                .setDescription(name)
                .setVisibleInDownloadsUi(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir("/" + c.getResources().getString(R.string.app_name),
                        name + URL)
                .allowScanningByMediaScanner();
        mgr.enqueue(request);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }


    public static void setFontForActivity(View view) {

        Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/theme.ttf");
        Typeface tfb = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/theme_bold.ttf");
        Typeface tfl = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/theme_light.ttf");
        //Set up touch listener for non-text box views to hide keyboard.
        setFontRecursive(view, tf, tfb, tfl);

    }

    private static void setFontRecursive(View view, Typeface tf, Typeface tfb, Typeface tfl) {
        if (view instanceof TextView || view instanceof EditText) {
            if (view.getTag() != null && view.getTag().equals("bold")) {
                ((TextView) view).setTypeface(tfb);
            } else {
                if (view.getTag() != null && view.getTag().equals("light")) {
                    ((TextView) view).setTypeface(tfl);
                } else {
                    ((TextView) view).setTypeface(tf);
                }
            }
        } else {
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setFontRecursive(innerView, tf, tfb, tfl);
                }
            }
        }
    }

    public static String getSP(String key, String defaultValue) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(Constants.SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setSP(String key, String value) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(Constants.SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(key, value);
        spe.apply();
    }

    public static void removeSP(String key) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(Constants.SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.remove(key);
        spe.apply();
    }

    public static int dpToPixels(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getDisplayWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }


    public static void setColorFilter(int iColor, ImageView image) {
        int red = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue = iColor & 0xFF;

        float[] matrix = {0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0};
        image.getDrawable().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void setupUI(final Activity activity, final View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(activity, innerView);
            }
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        } else {
            return true;
        }
    }
}
