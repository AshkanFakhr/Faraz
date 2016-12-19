package ashkan.fakhr.faraz.utilities;

import java.text.DecimalFormat;

import ashkan.fakhr.faraz.models.ValidationResponseModel;


public class Constants {

    private static String WEB_SERVER = "http://support.appchar.com/api/rest/public/";
    public static String SIGN_UP_URL = WEB_SERVER + "user";
    public static String LOGIN_URL = WEB_SERVER + "user/token";
    public static String TOPICS_URL = WEB_SERVER + "topics";


    //Name Strings:
    static String LOG_TAG = "****ASHKAN ** DEBUG****";

    // local


    //Shared Pref Keys
    static String SP_FILE_NAME_BASE = "sp_file_base";
    static String FALSE = "FALSE";
    public static String TRUE = "TRUE";

    //INTENT KEYS:
    public static String PARENT_ID = "PARENT_ID";
    public static String TITLE = "TITLE";
    public static String MAX_NUMBER = "MAX_NUMBER";
    public static String HEADER_IMAGE = "HEADER_IMAGE";
    public static String LINK = "LINK";
    public static String POSITION = "POSITION";
    public static String FROM_SPLASH = "FROM_SPLASH";
    public static String TYPE = "TYPE";
    public static String IMAGE = "IMAGE";
    public static String ID = "ID";

    // 0 : new items
    // 1 : top items
    public static String LIST_TYPE = "LIST_TYPE";
    public static final int TYPE_NEW_ITEMS = 0;
    public static final int TYPE_TOP_ITEMS = 1;
    public static final String TOP_ITEMS_PARAMETER = "&sortby=SortBy-5";
    public static final String NEW_ITEMS_PARAMETER = "&sortby=SortBy-2";

    public static String GROUP_ID = "GROUP_ID";
    public static String HOME_LIST_TYPE = "HOME_LIST_TYPE";
    public static String PRODUCT_ID = "PRODUCT_ID";
    public static String PRODUCT_LIST = "PRODUCT_LIST";
    public static String TECHNICAL_SPEC = "TECHNICAL_SPEC";
    public static String MODEL_JSON = "MODEL_JSON";


    //Numbers:
    public static int NOTIFICATION_CHECK_INTERVAL = 30 * 60 * 1000;
    public static final int VOLLEY_TIME_OUT = 25000;
    public static int COUNT = 7;


    public static final int NOTIFICATION_ID = 900;
    static boolean DEBUG = true;

    public static String persianNumbers(String num) {
        try {
            char[] persianNumbers = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < num.length(); i++) {
                if (Character.isDigit(num.charAt(i))) {
                    builder.append(persianNumbers[(int) (num.charAt(i)) - 48]);
                } else {
                    builder.append(num.charAt(i));
                }
            }
            return builder.toString();
        } catch (Exception e) {
            return num;
        }
    }

    public static String formatPrice(String num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(Float.valueOf(num));
    }

}
