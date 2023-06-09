package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility;


import com.google.gson.Gson;

import java.lang.reflect.Type;

public final class GsonUtils {
    public static String fromJsonToString(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static <T> T fromStringToJson(String data, Type type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(data, type);
        } catch (Exception ex) {
            return null;
        }
    }
}
