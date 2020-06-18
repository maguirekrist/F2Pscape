package org.skidperfect;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public class PriceHelper {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final Gson GSON = new Gson().newBuilder().create();

    private static final String OLDSCHOOL_RUNESCAPE_API_URL = "http://services.runescape.com/m=itemdb_oldschool/api/";

    public static int getRSPrice(int id) throws IOException {
        final Request request = new Request.Builder()
                .url(OLDSCHOOL_RUNESCAPE_API_URL + "catalogue/detail.json?item=" + id)
                .get()
                .build();

        final Response response = HTTP_CLIENT.newCall(request).execute();

        if (!response.isSuccessful() || response.body() == null)
            return -1;

        final String priceText = GSON.fromJson(response.body().string(), JsonObject.class)
                .getAsJsonObject("item")
                .getAsJsonObject("current")
                .get("price")
                .getAsString();

        final int price = Integer.parseInt(priceText.replaceAll("\\D+", ""));

        return priceText.matches("[0-9]+") ? price : price * (priceText.charAt(0) == 'k' ? 1000 : 1000000);
    }

    public static int getDailyRSPrice(int id) throws IOException {
        final Request request = new Request.Builder()
                .url(OLDSCHOOL_RUNESCAPE_API_URL + "graph/" + id + ".json")
                .get()
                .build();

        final Response response = HTTP_CLIENT.newCall(request).execute();

        if (!response.isSuccessful() || response.body() == null)
            return -1;

        final JsonObject jsonObject = GSON.fromJson(response.body().string(), JsonObject.class)
                .getAsJsonObject("daily")
                .getAsJsonObject();

        final int size = jsonObject.entrySet().size();
        final Map.Entry<String, JsonElement> entry = ((Map.Entry<String, JsonElement>) jsonObject.entrySet().toArray()[size - 1]);
        return Integer.parseInt(entry.getValue().getAsString());
    }

}
