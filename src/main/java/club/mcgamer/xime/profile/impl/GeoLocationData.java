package club.mcgamer.xime.profile.impl;

import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.TimeZone;

@Getter @Setter
public class GeoLocationData {

    private static final String API_KEY = "f42622ca3d29fc3bff5034ef3a58cc1c2fbb585bd4e85b78ea3c9065";

    private final String ipAddress;

    private final String city;

    private final String region;
    private final String regionCode;

    private final String country;
    private final String countryCode;

    private final String continent;
    private final String continentCode;

    private final String providerName;
    private final String providerDomain;
    private final String providerType;

    private final ZoneId timeZone;
    private final String timeZoneName;
    private final String timeZoneCode;

    private final boolean proxy;
    private final boolean dataCenter;
    private final boolean threat;

    public GeoLocationData(InetAddress ipAddress) {
        this(ipAddress.getHostAddress());
    }

    public GeoLocationData(String ipAddress) {
        this.ipAddress = ipAddress;

        JsonObject jsonResponse = get();

        this.city = jsonResponse.has("city") ? jsonResponse.get("city").getAsString() : null;
        this.region = jsonResponse.has("region") ? jsonResponse.get("region").getAsString() : null;
        this.regionCode = jsonResponse.has("region_code") ? jsonResponse.get("region_code").getAsString() : null;
        this.country = jsonResponse.has("country") ? jsonResponse.get("country").getAsString() : null;
        this.countryCode = jsonResponse.has("country_code") ? jsonResponse.get("country_code").getAsString() : null;
        this.continent = jsonResponse.has("continent") ? jsonResponse.get("continent").getAsString() : null;
        this.continentCode = jsonResponse.has("continent_code") ? jsonResponse.get("continent_code").getAsString() : null;
        this.providerName = jsonResponse.has("organisation") ? jsonResponse.getAsJsonObject("organisation").get("name").getAsString() : null;
        this.providerDomain = jsonResponse.has("organisation") ? jsonResponse.getAsJsonObject("organisation").get("domain").getAsString() : null;
        this.providerType = jsonResponse.has("organisation") ? jsonResponse.getAsJsonObject("organisation").get("type").getAsString() : null;
        this.timeZoneName = jsonResponse.has("time_zone") ? jsonResponse.getAsJsonObject("time_zone").get("name").getAsString() : null;
        this.timeZoneCode = jsonResponse.has("time_zone") ? jsonResponse.getAsJsonObject("time_zone").get("abbr").getAsString() : null;
        this.timeZone = ZoneId.of(timeZoneName);
        this.proxy = jsonResponse.has("threat") && jsonResponse.getAsJsonObject("threat").get("is_proxy").getAsBoolean();
        this.dataCenter = jsonResponse.has("threat") && jsonResponse.getAsJsonObject("threat").get("is_datacenter").getAsBoolean();
        this.threat = jsonResponse.has("threat") && jsonResponse.getAsJsonObject("threat").get("is_threat").getAsBoolean();
    }

    @SneakyThrows
    private JsonObject get() {
        URL url = new URL("https://api.ipdata.co/" + ipAddress + "?api-key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        // Check for successful response code
        if (conn.getResponseCode() != 200)
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());


        // Read the response
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            response.append(line);

        br.close();
        conn.disconnect();

        // Parse JSON using Gson
        return JsonParser.parseString(response.toString()).getAsJsonObject();
    }

}
