package org.yestech.rpx;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONException;
import org.json.JSONObject;
import org.yestech.rpx.auth.*;
import org.yestech.rpx.objectmodel.*;
import static org.yestech.rpx.objectmodel.RPXUtil.jsonString;
import static org.yestech.rpx.RPXClient.Provider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author A.J. Wright
 */
public class DefaultRPXClient implements RPXClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRPXClient.class);

    private static final String RPX_API_URL = "https://rpxnow.com/api/v2/";

    private String apiKey;
    private String realm;

    public DefaultRPXClient(String apiKey, String realm) {
        this.apiKey = apiKey;
        this.realm = realm;
    }

    public AuthInfoResponse authInfo(String token, boolean extended) throws IOException, JSONException, RPXException {

        StringBuilder url = new StringBuilder(RPX_API_URL);
        url.append("auth_info");
        url.append("?token=").append(token);
        url.append("&apiKey=").append(apiKey);
        url.append("&extended=").append(extended);

        HttpClient client = getHttpClient();
        GetMethod get = new GetMethod(url.toString());
        try {
            client.executeMethod(get);
            String body = get.getResponseBodyAsString();
            JSONObject jo = new JSONObject(body);
            RPXException ex = RPXException.fromJSON(jo);
            if (ex != null) throw ex; // if the response was an exception throw it.

            // If not continue on
            return AuthInfoResponse.fromJson(jo);
        } finally {
            get.releaseConnection();
        }
    }

    public RPXStat map(String identifier, String primaryKey, boolean overwrite) throws IOException, JSONException, RPXException {

        StringBuilder url = new StringBuilder(RPX_API_URL);
        url.append("map");
        url.append("?identifier=").append(identifier);
        url.append("&primaryKey=").append(primaryKey);
        url.append("&overwrite=").append(overwrite);

        HttpClient client = getHttpClient();
        GetMethod get = new GetMethod(url.toString());
        try {
            client.executeMethod(get);
            String body = get.getResponseBodyAsString();
            JSONObject jo = new JSONObject(body);
            RPXException ex = RPXException.fromJSON(jo);
            if (ex != null) throw ex;

            return RPXStat.fromString(jsonString(jo, "stat"));
        } finally {
            get.releaseConnection();
        }
    }

    public MappingsResponse getMappings(String primaryKey) throws IOException, JSONException, RPXException {

        StringBuilder url = new StringBuilder(RPX_API_URL)
                .append("mappings")
                .append("?apiKey=").append(apiKey)
                .append("&primaryKey=").append(primaryKey);

        HttpClient client = getHttpClient();
        GetMethod get = new GetMethod(url.toString());
        try {
            client.executeMethod(get);
            String body = get.getResponseBodyAsString();
            JSONObject jo = new JSONObject(body);
            RPXException ex = RPXException.fromJSON(jo);
            if (ex != null) throw ex;

            return MappingsResponse.fromJson(jo);
        } finally {
            get.releaseConnection();
        }


    }

    public GetContactsResponse getContacts(String identifier) throws JSONException, IOException, RPXException {
        StringBuilder url = new StringBuilder(RPX_API_URL);
        url.append("get_contacts");
        url.append("?apiKey=").append(apiKey);
        url.append("&identifier=").append(identifier);

        GetMethod get = new GetMethod(url.toString());

        try {
            HttpClient client = getHttpClient();
            client.executeMethod(get);
            String body = get.getResponseBodyAsString();
            JSONObject jo = new JSONObject(body);
            RPXException ex = RPXException.fromJSON(jo);
            if (ex != null) throw ex;

            return GetContactsResponse.fromJson(jo);
        } finally {
            get.releaseConnection();
        }
    }

    public String buildAuthRedirect(Provider provider, String tokenUrl) throws IOException {

        if (provider == MICROSOFT_LIVE) {
            return buildAuthRedirect(new MicrosoftLiveProvider(), tokenUrl);
        }
        else if (provider == GOOGLE) {
            return buildAuthRedirect(new GoogleAuthProvider(), tokenUrl);
        }
        else if (provider == FACEBOOK) {
             return buildAuthRedirect(new FacebookAuthProvider(), tokenUrl);
        }
        else if (provider == TWITTER) {
             return buildAuthRedirect(new TwitterAuthProvider(), tokenUrl);
        }
        throw new IllegalArgumentException("unknown provider"+provider); //shouldn't happen
    }

    public void setStatus(String identifier, String status) throws IOException, JSONException, RPXException {
        GetMethod get = null;
        try {
            StringBuilder url = new StringBuilder(RPX_API_URL)
                    .append("set_status")
                    .append("?apiKey=").append(apiKey)
                    .append("&identifier").append(identifier)
                    .append("status").append(URLEncoder.encode(status, "UTF-8"));

            get = new GetMethod(url.toString());
            HttpClient client = getHttpClient();
            client.executeMethod(get);
            String body = get.getResponseBodyAsString();
            JSONObject jo = new JSONObject(body);
            RPXException ex = RPXException.fromJSON(jo);
            if (ex != null) throw ex;


        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            if (get != null) get.releaseConnection();
        }
    }

    public String buildAuthRedirect(RPXAuthProvider provider, String tokenUrl) {
        return provider.getRedirectUrl(realm, RPXUtil.uriEncode(tokenUrl));
    }


    protected HttpClient getHttpClient() {
        return new HttpClient();
    }

}
