package org.yestech.rpx.objectmodel;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.io.IOUtil;
import org.json.JSONObject;
import org.json.JSONException;
import static org.yestech.rpx.objectmodel.RPXUtil.fromRPXDateString;
import static org.yestech.rpx.objectmodel.Gender.MALE;

import java.io.*;

import static junit.framework.Assert.assertTrue;


/**
 * @author A.J. Wright
 */
public class AuthInfoResponseTest {

    @Test
    public void testMyOpenIdResponse() throws IOException, JSONException {

        JSONObject json = loadFile("/aj-myopenid-authInfo.json");
        AuthInfoResponse response = AuthInfoResponse.fromJson(json);

        assertEquals(fromRPXDateString("1979-01-09"), response.getSreg().getDob());
        assertEquals("A.J.", response.getSreg().getNickname());
        assertEquals("Andrew Wright", response.getSreg().getFullname());
        assertEquals(MALE, response.getSreg().getGender());
        assertEquals("ayax79@gmail.com", response.getSreg().getEmail());

        assertEquals("Andrew Wright", response.getProfile().getName().getFormatted());
        assertEquals("Andrew Wright", response.getProfile().getDisplayname());
        assertEquals("A.J.", response.getProfile().getPreferredUsername());
        assertEquals(MALE, response.getProfile().getGender());
        assertEquals(fromRPXDateString("1979-01-09"), response.getProfile().getBirthday());
        assertEquals("MyOpenID", response.getProfile().getProviderName());
        assertEquals("http://ayax79.myopenid.com/", response.getProfile().getIdentifier());
        assertEquals("ayax79@gmail.com", response.getProfile().getEmail());

        assertEquals(MALE, response.getMergedPoco().getGender());
        assertEquals("other", response.getMergedPoco().getUrls().get(0).getType());
        assertEquals("http://ayax79.myopenid.com/", response.getMergedPoco().getUrls().get(0).getValue());
        assertEquals("A.J.", response.getMergedPoco().getPreferredUsername());
        assertEquals("Andrew Wright", response.getMergedPoco().getDisplayname());
        assertEquals("Andrew Wright", response.getMergedPoco().getName().getFormatted());
        assertEquals(fromRPXDateString("1979-01-09"), response.getMergedPoco().getBirthday());
        assertEquals("other", response.getMergedPoco().getEmails().get(0).getType());
        assertEquals("ayax79@gmail.com", response.getMergedPoco().getEmails().get(0).getValue());
        assertEquals(RPXStat.OK, response.getStat());
    }

    public void testGmailResponse() throws JSONException, IOException {
        JSONObject json = loadFile("/aj-gmail-authinfo.json");
        AuthInfoResponse response = AuthInfoResponse.fromJson(json);

        assertEquals("ayax79@gmail.com", response.getProfile().getVerifiedEmail());
        assertEquals("Andrew", response.getProfile().getName().getGivenName());
        assertEquals("Wright", response.getProfile().getName().getFamilyName());
        assertEquals("Andrew Wright", response.getProfile().getName().getFormatted());
        assertEquals("ayax79", response.getProfile().getDisplayname());
        assertEquals("ayax79", response.getProfile().getPreferredUsername());
        assertEquals("Google", response.getProfile().getProviderName());
        assertEquals("https://www.google.com/accounts/o8/id?id=AItOawlGnDTam-IKbSzIiX76G_3R5uYleBpJgQc",
                response.getProfile().getIdentifier());
        assertEquals("ayax79@gmail.com", response.getProfile().getEmail());

        assertEquals("en-US", response.getMergedPoco().getLanguageSpoken().get(0));
        assertEquals("Andrew", response.getMergedPoco().getName().getGivenName());
        assertEquals("Wright", response.getMergedPoco().getName().getFamilyName());
        assertEquals("Andrew Wright", response.getMergedPoco().getName().getFormatted());
        assertEquals("ayax79", response.getMergedPoco().getPreferredUsername());
        assertEquals("other", response.getMergedPoco().getEmails().get(0).getType());
        assertEquals("ayax79@gmail.com", response.getMergedPoco().getEmails().get(0).getValue());
        assertEquals(RPXStat.OK, response.getStat());
    }

    public void testFacebookResponse() throws JSONException, IOException {
        JSONObject json = loadFile("/aj-facebook-authinfo.json");
        AuthInfoResponse response = AuthInfoResponse.fromJson(json);

        for (String f : facebookFriends) {
            assertTrue(response.getFriends().contains(f));
        }

        assertEquals("A.j.", response.getProfile().getName().getGivenName());
        assertEquals("Wright", response.getProfile().getName().getFamilyName());
        assertEquals("A.j. Wright", response.getProfile().getName().getFormatted());
        assertEquals("Oregon", response.getProfile().getAddress().getRegion());
        assertEquals("United States", response.getProfile().getAddress().getCountry());
        assertEquals("97068", response.getProfile().getAddress().getPostalCode());
        assertEquals("West Linn", response.getProfile().getAddress().getLocality());
        assertEquals("http://profile.ak.fbcdn.net/v226/90/68/n543888265_6128.jpg", response.getProfile().getPhoto());
        assertEquals("A.j. Wright", response.getProfile().getPreferredUsername());
        assertEquals("http://www.facebook.com/profile.php?id=543888265", response.getProfile().getIdentifier());

        assertEquals("1250226000", response.getAccessCredentials().getExpires());
        assertEquals("543888265", response.getAccessCredentials().getUid());
        assertEquals("Facebook", response.getAccessCredentials().getType());
        assertEquals("2.YuyncfQy_5WZ1YwUvZeomA__.86400.1250226000-543888265", response.getAccessCredentials().getSessionKey());

        assertEquals(Gender.MALE, response.getMergedPoco().getGender());

        String[] movies = {"Once", "The Royal Tenenbaums", "Lord Of The Rings"};
        for (String movie : movies) {
            assertTrue(response.getMergedPoco().getMovies().contains(movie));
        }

        assertEquals("United States", response.getMergedPoco().getCurrentLocation().getCountry());
        assertEquals("Oregon", response.getMergedPoco().getCurrentLocation().getRegion());
        assertEquals("97068", response.getMergedPoco().getCurrentLocation().getPostalCode());
        assertEquals("West Linn", response.getMergedPoco().getCurrentLocation().getLocality());

        assertEquals("profile", response.getMergedPoco().getUrls().get(0).getType());
        assertEquals("http://www.facebook.com/ayax79", response.getMergedPoco().getUrls().get(0).getValue());

        String[] tvShows = {"thirty rock", "simpsons", "family guy"};
        for (String tvShow : tvShows) {
            assertTrue(response.getMergedPoco().getTvShows().contains(tvShow));
        }

        assertEquals("-07:00", response.getMergedPoco().getUtcOffset());
        assertEquals("other", response.getMergedPoco().getPhotos().get(0).getType());
        assertEquals("http://profile.ak.fbcdn.net/v226/90/68/n543888265_6128.jpg", response.getMergedPoco().getPhotos().get(0).getValue());

        for (String music : facebookMusic) {
            assertTrue(response.getMergedPoco().getMusic().contains(music));
        }




    }

    private JSONObject loadFile(String file) throws IOException, JSONException {
        InputStream in = getClass().getResourceAsStream(file);
        Reader reader = new InputStreamReader(in);
        StringWriter writer = new StringWriter();
        IOUtil.copy(reader, writer);
        return new JSONObject(writer.toString());
    }

    String[] facebookMusic = new String[]{
            "Modest Mouse",
            "Built to Spill",
            "The Shins",
            "Sigur Ros",
            "Death cab for cutie",
            "interpol",
            "Jack Johnson",
            "Led Zeppelin",
            "The Killers",
            "Charles Mingus",
            "Pink Floyd",
            "Radiohead",
            "Rilo Kiley",
            "Snow Patrol",
            "The Mars Volta",
            "Primus",
            "The Decemberists",
            "Stars of Track and Field",
            "The White Stripes",
            "Stone Temple Pilots",
            "Smashing Pumpkins",
            "Pearl Jam",
            "NOFX",
            "Queens of the Stone Age",
            "Mono",
            "Viva Voce",
            "Ivy",
            "Airborne Toxic Event"
    };


    String[] facebookFriends = new String[]{
            "http://www.facebook.com/profile.php?id=8345294",
            "http://www.facebook.com/profile.php?id=19705776",
            "http://www.facebook.com/profile.php?id=19709202",
            "http://www.facebook.com/profile.php?id=19710273",
            "http://www.facebook.com/profile.php?id=19718453",
            "http://www.facebook.com/profile.php?id=19719552",
            "http://www.facebook.com/profile.php?id=29300496",
            "http://www.facebook.com/profile.php?id=29301426",
            "http://www.facebook.com/profile.php?id=42004023",
            "http://www.facebook.com/profile.php?id=42005368",
            "http://www.facebook.com/profile.php?id=51700998",
            "http://www.facebook.com/profile.php?id=51702847",
            "http://www.facebook.com/profile.php?id=149700233",
            "http://www.facebook.com/profile.php?id=500062544",
            "http://www.facebook.com/profile.php?id=500067275",
            "http://www.facebook.com/profile.php?id=500067426",
            "http://www.facebook.com/profile.php?id=501121957",
            "http://www.facebook.com/profile.php?id=501336901",
            "http://www.facebook.com/profile.php?id=504091058",
            "http://www.facebook.com/profile.php?id=504167135",
            "http://www.facebook.com/profile.php?id=506749663",
            "http://www.facebook.com/profile.php?id=507180053",
            "http://www.facebook.com/profile.php?id=508836673",
            "http://www.facebook.com/profile.php?id=509477037",
            "http://www.facebook.com/profile.php?id=512234367",
            "http://www.facebook.com/profile.php?id=516045635",
            "http://www.facebook.com/profile.php?id=516297787",
            "http://www.facebook.com/profile.php?id=523577505",
            "http://www.facebook.com/profile.php?id=526362535",
            "http://www.facebook.com/profile.php?id=526679936",
            "http://www.facebook.com/profile.php?id=527705761",
            "http://www.facebook.com/profile.php?id=534688081",
            "http://www.facebook.com/profile.php?id=536694637",
            "http://www.facebook.com/profile.php?id=537120168",
            "http://www.facebook.com/profile.php?id=537840299",
            "http://www.facebook.com/profile.php?id=537897802",
            "http://www.facebook.com/profile.php?id=539944201",
            "http://www.facebook.com/profile.php?id=540072003",
            "http://www.facebook.com/profile.php?id=540091228",
            "http://www.facebook.com/profile.php?id=540734088",
            "http://www.facebook.com/profile.php?id=541874142",
            "http://www.facebook.com/profile.php?id=542162808",
            "http://www.facebook.com/profile.php?id=542502882",
            "http://www.facebook.com/profile.php?id=546257030",
            "http://www.facebook.com/profile.php?id=546353693",
            "http://www.facebook.com/profile.php?id=549095026",
            "http://www.facebook.com/profile.php?id=549981450",
            "http://www.facebook.com/profile.php?id=554787116",
            "http://www.facebook.com/profile.php?id=555135736",
            "http://www.facebook.com/profile.php?id=564388078",
            "http://www.facebook.com/profile.php?id=567430071",
            "http://www.facebook.com/profile.php?id=568619318",
            "http://www.facebook.com/profile.php?id=568708145",
            "http://www.facebook.com/profile.php?id=568743293",
            "http://www.facebook.com/profile.php?id=577039108",
            "http://www.facebook.com/profile.php?id=577394558",
            "http://www.facebook.com/profile.php?id=578827425",
            "http://www.facebook.com/profile.php?id=578827786",
            "http://www.facebook.com/profile.php?id=578893453",
            "http://www.facebook.com/profile.php?id=579593838",
            "http://www.facebook.com/profile.php?id=579596226",
            "http://www.facebook.com/profile.php?id=580163848",
            "http://www.facebook.com/profile.php?id=580207929",
            "http://www.facebook.com/profile.php?id=580888619",
            "http://www.facebook.com/profile.php?id=584723221",
            "http://www.facebook.com/profile.php?id=585934552",
            "http://www.facebook.com/profile.php?id=586231075",
            "http://www.facebook.com/profile.php?id=587454534",
            "http://www.facebook.com/profile.php?id=588894435",
            "http://www.facebook.com/profile.php?id=590231955",
            "http://www.facebook.com/profile.php?id=591207915",
            "http://www.facebook.com/profile.php?id=591604378",
            "http://www.facebook.com/profile.php?id=600769563",
            "http://www.facebook.com/profile.php?id=603572784",
            "http://www.facebook.com/profile.php?id=603654079",
            "http://www.facebook.com/profile.php?id=605637363",
            "http://www.facebook.com/profile.php?id=608131600",
            "http://www.facebook.com/profile.php?id=608990188",
            "http://www.facebook.com/profile.php?id=610579477",
            "http://www.facebook.com/profile.php?id=612514282",
            "http://www.facebook.com/profile.php?id=613112232",
            "http://www.facebook.com/profile.php?id=616273316",
            "http://www.facebook.com/profile.php?id=616549417",
            "http://www.facebook.com/profile.php?id=617243017",
            "http://www.facebook.com/profile.php?id=620378773",
            "http://www.facebook.com/profile.php?id=620859611",
            "http://www.facebook.com/profile.php?id=621755277",
            "http://www.facebook.com/profile.php?id=621903947",
            "http://www.facebook.com/profile.php?id=622138288",
            "http://www.facebook.com/profile.php?id=622364781",
            "http://www.facebook.com/profile.php?id=622372924",
            "http://www.facebook.com/profile.php?id=622544831",
            "http://www.facebook.com/profile.php?id=622577891",
            "http://www.facebook.com/profile.php?id=622703274",
            "http://www.facebook.com/profile.php?id=622912941",
            "http://www.facebook.com/profile.php?id=623069988",
            "http://www.facebook.com/profile.php?id=623109055",
            "http://www.facebook.com/profile.php?id=625563568",
            "http://www.facebook.com/profile.php?id=627316135",
            "http://www.facebook.com/profile.php?id=629673545",
            "http://www.facebook.com/profile.php?id=630612128",
            "http://www.facebook.com/profile.php?id=631071004",
            "http://www.facebook.com/profile.php?id=635860349",
            "http://www.facebook.com/profile.php?id=647831002",
            "http://www.facebook.com/profile.php?id=648255623",
            "http://www.facebook.com/profile.php?id=649526712",
            "http://www.facebook.com/profile.php?id=650001285",
            "http://www.facebook.com/profile.php?id=650699551",
            "http://www.facebook.com/profile.php?id=652355416",
            "http://www.facebook.com/profile.php?id=653551779",
            "http://www.facebook.com/profile.php?id=653674831",
            "http://www.facebook.com/profile.php?id=655528915",
            "http://www.facebook.com/profile.php?id=659093695",
            "http://www.facebook.com/profile.php?id=662063377",
            "http://www.facebook.com/profile.php?id=664286491",
            "http://www.facebook.com/profile.php?id=666293152",
            "http://www.facebook.com/profile.php?id=671001147",
            "http://www.facebook.com/profile.php?id=672048311",
            "http://www.facebook.com/profile.php?id=674421319",
            "http://www.facebook.com/profile.php?id=675774449",
            "http://www.facebook.com/profile.php?id=678663626",
            "http://www.facebook.com/profile.php?id=680262938",
            "http://www.facebook.com/profile.php?id=681682345",
            "http://www.facebook.com/profile.php?id=681878615",
            "http://www.facebook.com/profile.php?id=685319012",
            "http://www.facebook.com/profile.php?id=685908409",
            "http://www.facebook.com/profile.php?id=687405950",
            "http://www.facebook.com/profile.php?id=687733752",
            "http://www.facebook.com/profile.php?id=688556535",
            "http://www.facebook.com/profile.php?id=690057037",
            "http://www.facebook.com/profile.php?id=690240860",
            "http://www.facebook.com/profile.php?id=692000769",
            "http://www.facebook.com/profile.php?id=692506628",
            "http://www.facebook.com/profile.php?id=695084362",
            "http://www.facebook.com/profile.php?id=696161158",
            "http://www.facebook.com/profile.php?id=701661942",
            "http://www.facebook.com/profile.php?id=702524034",
            "http://www.facebook.com/profile.php?id=704631512",
            "http://www.facebook.com/profile.php?id=705204186",
            "http://www.facebook.com/profile.php?id=707411018",
            "http://www.facebook.com/profile.php?id=711417758",
            "http://www.facebook.com/profile.php?id=713364232",
            "http://www.facebook.com/profile.php?id=715600788",
            "http://www.facebook.com/profile.php?id=716041681",
            "http://www.facebook.com/profile.php?id=720280155",
            "http://www.facebook.com/profile.php?id=720569235",
            "http://www.facebook.com/profile.php?id=720906457",
            "http://www.facebook.com/profile.php?id=721327089",
            "http://www.facebook.com/profile.php?id=722105282",
            "http://www.facebook.com/profile.php?id=722447044",
            "http://www.facebook.com/profile.php?id=726770477",
            "http://www.facebook.com/profile.php?id=727823792",
            "http://www.facebook.com/profile.php?id=728303210",
            "http://www.facebook.com/profile.php?id=728945065",
            "http://www.facebook.com/profile.php?id=732361629",
            "http://www.facebook.com/profile.php?id=737065105",
            "http://www.facebook.com/profile.php?id=747680534",
            "http://www.facebook.com/profile.php?id=747790466",
            "http://www.facebook.com/profile.php?id=753610610",
            "http://www.facebook.com/profile.php?id=753863864",
            "http://www.facebook.com/profile.php?id=755284702",
            "http://www.facebook.com/profile.php?id=756949946",
            "http://www.facebook.com/profile.php?id=760675197",
            "http://www.facebook.com/profile.php?id=765986872",
            "http://www.facebook.com/profile.php?id=770290689",
            "http://www.facebook.com/profile.php?id=771842245",
            "http://www.facebook.com/profile.php?id=772310228",
            "http://www.facebook.com/profile.php?id=778111893",
            "http://www.facebook.com/profile.php?id=783473018",
            "http://www.facebook.com/profile.php?id=784176348",
            "http://www.facebook.com/profile.php?id=791202345",
            "http://www.facebook.com/profile.php?id=791675954",
            "http://www.facebook.com/profile.php?id=797409165",
            "http://www.facebook.com/profile.php?id=797842976",
            "http://www.facebook.com/profile.php?id=803228850",
            "http://www.facebook.com/profile.php?id=812314920",
            "http://www.facebook.com/profile.php?id=816019889",
            "http://www.facebook.com/profile.php?id=823949062",
            "http://www.facebook.com/profile.php?id=827314285",
            "http://www.facebook.com/profile.php?id=834578046",
            "http://www.facebook.com/profile.php?id=837387305",
            "http://www.facebook.com/profile.php?id=868605594",
            "http://www.facebook.com/profile.php?id=1000910517",
            "http://www.facebook.com/profile.php?id=1008349695",
            "http://www.facebook.com/profile.php?id=1024860864",
            "http://www.facebook.com/profile.php?id=1024939401",
            "http://www.facebook.com/profile.php?id=1028682952",
            "http://www.facebook.com/profile.php?id=1031135875",
            "http://www.facebook.com/profile.php?id=1034021801",
            "http://www.facebook.com/profile.php?id=1041004926",
            "http://www.facebook.com/profile.php?id=1043983958",
            "http://www.facebook.com/profile.php?id=1058094173",
            "http://www.facebook.com/profile.php?id=1060124782",
            "http://www.facebook.com/profile.php?id=1074018470",
            "http://www.facebook.com/profile.php?id=1075656324",
            "http://www.facebook.com/profile.php?id=1078518473",
            "http://www.facebook.com/profile.php?id=1086351290",
            "http://www.facebook.com/profile.php?id=1087759968",
            "http://www.facebook.com/profile.php?id=1088655931",
            "http://www.facebook.com/profile.php?id=1102381698",
            "http://www.facebook.com/profile.php?id=1107238160",
            "http://www.facebook.com/profile.php?id=1127689937",
            "http://www.facebook.com/profile.php?id=1129415074",
            "http://www.facebook.com/profile.php?id=1149568480",
            "http://www.facebook.com/profile.php?id=1167424687",
            "http://www.facebook.com/profile.php?id=1174536648",
            "http://www.facebook.com/profile.php?id=1177681625",
            "http://www.facebook.com/profile.php?id=1182725316",
            "http://www.facebook.com/profile.php?id=1186312041",
            "http://www.facebook.com/profile.php?id=1191157841",
            "http://www.facebook.com/profile.php?id=1191217822",
            "http://www.facebook.com/profile.php?id=1192103605",
            "http://www.facebook.com/profile.php?id=1193381383",
            "http://www.facebook.com/profile.php?id=1194023603",
            "http://www.facebook.com/profile.php?id=1195388205",
            "http://www.facebook.com/profile.php?id=1203900928",
            "http://www.facebook.com/profile.php?id=1206354672",
            "http://www.facebook.com/profile.php?id=1207014337",
            "http://www.facebook.com/profile.php?id=1231171925",
            "http://www.facebook.com/profile.php?id=1232121455",
            "http://www.facebook.com/profile.php?id=1237984510",
            "http://www.facebook.com/profile.php?id=1247168457",
            "http://www.facebook.com/profile.php?id=1248046906",
            "http://www.facebook.com/profile.php?id=1248668319",
            "http://www.facebook.com/profile.php?id=1252326010",
            "http://www.facebook.com/profile.php?id=1255612338",
            "http://www.facebook.com/profile.php?id=1257015895",
            "http://www.facebook.com/profile.php?id=1266641641",
            "http://www.facebook.com/profile.php?id=1267088506",
            "http://www.facebook.com/profile.php?id=1268623105",
            "http://www.facebook.com/profile.php?id=1275318960",
            "http://www.facebook.com/profile.php?id=1291838919",
            "http://www.facebook.com/profile.php?id=1291861129",
            "http://www.facebook.com/profile.php?id=1297832066",
            "http://www.facebook.com/profile.php?id=1302312430",
            "http://www.facebook.com/profile.php?id=1306075437",
            "http://www.facebook.com/profile.php?id=1311253751",
            "http://www.facebook.com/profile.php?id=1315711510",
            "http://www.facebook.com/profile.php?id=1317503208",
            "http://www.facebook.com/profile.php?id=1325954935",
            "http://www.facebook.com/profile.php?id=1340797371",
            "http://www.facebook.com/profile.php?id=1344758936",
            "http://www.facebook.com/profile.php?id=1348357501",
            "http://www.facebook.com/profile.php?id=1358180201",
            "http://www.facebook.com/profile.php?id=1362625817",
            "http://www.facebook.com/profile.php?id=1377216090",
            "http://www.facebook.com/profile.php?id=1378303187",
            "http://www.facebook.com/profile.php?id=1381412298",
            "http://www.facebook.com/profile.php?id=1383117959",
            "http://www.facebook.com/profile.php?id=1390065781",
            "http://www.facebook.com/profile.php?id=1397366980",
            "http://www.facebook.com/profile.php?id=1407086949",
            "http://www.facebook.com/profile.php?id=1416006009",
            "http://www.facebook.com/profile.php?id=1426188936",
            "http://www.facebook.com/profile.php?id=1428309640",
            "http://www.facebook.com/profile.php?id=1435959873",
            "http://www.facebook.com/profile.php?id=1439418290",
            "http://www.facebook.com/profile.php?id=1442720799",
            "http://www.facebook.com/profile.php?id=1445725671",
            "http://www.facebook.com/profile.php?id=1447533444",
            "http://www.facebook.com/profile.php?id=1450833139",
            "http://www.facebook.com/profile.php?id=1451641715",
            "http://www.facebook.com/profile.php?id=1452663422",
            "http://www.facebook.com/profile.php?id=1455570031",
            "http://www.facebook.com/profile.php?id=1457232485",
            "http://www.facebook.com/profile.php?id=1460556658",
            "http://www.facebook.com/profile.php?id=1467106626",
            "http://www.facebook.com/profile.php?id=1467261793",
            "http://www.facebook.com/profile.php?id=1469693531",
            "http://www.facebook.com/profile.php?id=1469727425",
            "http://www.facebook.com/profile.php?id=1475153008",
            "http://www.facebook.com/profile.php?id=1476191083",
            "http://www.facebook.com/profile.php?id=1476843542",
            "http://www.facebook.com/profile.php?id=1477391372",
            "http://www.facebook.com/profile.php?id=1480437395",
            "http://www.facebook.com/profile.php?id=1493147925",
            "http://www.facebook.com/profile.php?id=1495853632",
            "http://www.facebook.com/profile.php?id=1496401013",
            "http://www.facebook.com/profile.php?id=1499420313",
            "http://www.facebook.com/profile.php?id=1509123945",
            "http://www.facebook.com/profile.php?id=1524355657",
            "http://www.facebook.com/profile.php?id=1528914214",
            "http://www.facebook.com/profile.php?id=1531495228",
            "http://www.facebook.com/profile.php?id=1537811304",
            "http://www.facebook.com/profile.php?id=1539922159",
            "http://www.facebook.com/profile.php?id=1541748160",
            "http://www.facebook.com/profile.php?id=1558711397",
            "http://www.facebook.com/profile.php?id=1559043883",
            "http://www.facebook.com/profile.php?id=1559070217",
            "http://www.facebook.com/profile.php?id=1559286116",
            "http://www.facebook.com/profile.php?id=1574216767",
            "http://www.facebook.com/profile.php?id=1576790055",
            "http://www.facebook.com/profile.php?id=1578423484",
            "http://www.facebook.com/profile.php?id=1583583562",
            "http://www.facebook.com/profile.php?id=1593326962",
            "http://www.facebook.com/profile.php?id=1596038647",
            "http://www.facebook.com/profile.php?id=1599499837",
            "http://www.facebook.com/profile.php?id=1599850839",
            "http://www.facebook.com/profile.php?id=1624765603",
            "http://www.facebook.com/profile.php?id=1634346455",
            "http://www.facebook.com/profile.php?id=1636219055",
            "http://www.facebook.com/profile.php?id=1645644833",
            "http://www.facebook.com/profile.php?id=1654136803",
            "http://www.facebook.com/profile.php?id=1656814711",
            "http://www.facebook.com/profile.php?id=1671429568",
            "http://www.facebook.com/profile.php?id=1685842558",
            "http://www.facebook.com/profile.php?id=1759284947",
            "http://www.facebook.com/profile.php?id=1762457708",
            "http://www.facebook.com/profile.php?id=1799245807",
            "http://www.facebook.com/profile.php?id=1800366628",
            "http://www.facebook.com/profile.php?id=1846179985",
            "http://www.facebook.com/profile.php?id=100000042084000",
            "http://www.facebook.com/profile.php?id=100000047173252",
            "http://www.facebook.com/profile.php?id=100000052376153",
            "http://www.facebook.com/profile.php?id=100000058036602",
            "http://www.facebook.com/profile.php?id=100000061853790",
            "http://www.facebook.com/profile.php?id=100000077172283"
    };
}
