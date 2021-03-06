package org.yestech.rpx;

import org.yestech.rpx.objectmodel.AuthInfoResponse;
import org.yestech.rpx.objectmodel.RPXStat;
import org.yestech.rpx.objectmodel.RPXException;
import org.yestech.rpx.objectmodel.GetContactsResponse;
import org.yestech.rpx.auth.RPXAuthProvider;
import org.json.JSONException;

import java.io.IOException;

/**
 * @author A.J. Wright
 */
public interface RPXClient {
    String buildAuthRedirect(Provider provider, String tokenUrl) throws IOException;

    String buildAuthRedirect(RPXAuthProvider provider, String tokenUrl);

    void setStatus(String identifier, String status) throws IOException, JSONException, RPXException;

    static enum Provider {
        GOOGLE,
        MICROSOFT_LIVE,
        FACEBOOK,
        TWITTER
    }

    AuthInfoResponse authInfo(String token, boolean extended) throws Exception;

    RPXStat map(String identifier, String primaryKey, boolean overwrite) throws IOException, JSONException, RPXException;

    GetContactsResponse getContacts(String identifier) throws JSONException, IOException, RPXException;
}
