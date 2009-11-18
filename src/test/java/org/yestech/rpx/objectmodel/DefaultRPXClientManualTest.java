package org.yestech.rpx.objectmodel;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.yestech.rpx.DefaultRPXClient;

/**
 * @author A.J. Wright
 */
public class DefaultRPXClientManualTest {

    private String apiKey;
    private String realm;


    @Before
    public void setup() {
        apiKey = System.getProperty("test.apiKey");
        assertNotNull(apiKey);
        realm = System.getProperty("test.realm");
        assertNotNull(realm);
    }




    @Test
    public void authInfoTest() {

        DefaultRPXClient client = buildClient();





    }

    private DefaultRPXClient buildClient() {
        return new DefaultRPXClient(apiKey, realm);
    }

}
