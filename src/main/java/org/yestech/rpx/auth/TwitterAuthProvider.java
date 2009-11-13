package org.yestech.rpx.auth;

import static org.yestech.rpx.objectmodel.RPXUtil.uriEncode;

import static java.lang.String.format;

/**
 * Provides RPX authentication to Twitter
 *
 * @author Andy Nelsen
 * @version 0.1
 */
public class TwitterAuthProvider implements RPXAuthProvider {
    public static final String URL_PATTERN = "https://%s.rpxnow.com/twitter/start?token_url=%s";

    public String getRedirectUrl(String realm, String tokenUrl) {
        String encodedTokenUrl = uriEncode(tokenUrl);
        return format(URL_PATTERN, realm, encodedTokenUrl);
    }
}
