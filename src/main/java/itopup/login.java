package itopup;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.security.PrivateKey;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class login extends baseRequest {

    String username;
    String password;
    PrivateKey privateKey;
    String url;
    public login( String username, String password, PrivateKey privateKey, String url)
    {
        this.username =username;
        this.password = password;
        this.privateKey = privateKey;
        this.url= url;
    }
    public String sendRequest(String url, String request) throws Exception{
         CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(request);
            post.setEntity(entity);
            post.setHeader("Content-Type", "text/xml");
            post.setHeader("SoapAction","''");
            return EntityUtils.toString(client.execute(post).getEntity());
    }
public String getInfo(String decodedJson,String info) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(decodedJson);
        return rootNode.get(info).asText();

}

public  String requestHandle(int operation, String username, String password, PrivateKey privateKey, String url) throws Exception {
    String data = username +"|" + password;
    System.out.println("Data:" + data);
    String signature = sign(data, privateKey);
    String requestBody ="{\"operation\":"+operation+", \n"
            + "                \"username\":\""+username+"\", \n"
            + "                \"merchantPass\":\"" +password + "\",\n "
            + "                \"signature\":\"" +signature+"\"}";
    String request = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <Body>\n" +
            "        <requestHandle xmlns=\"http://interfaces.itopup.vnptepay.vn\">\n" +
            "            <requestData>"+ requestBody+"</requestData>\n" +
            "        </requestHandle>\n" +
            "    </Body>\n" +
            "</Envelope>";

    System.out.println("Request Login: "+ request);
    return sendRequest(url, request);

}
    public static void main(String[] args) throws Exception {
        baseRequest base = new baseRequest();
        String url = "https://haloship.imediatech.com.vn:8087/ItopupService2.0_IMD/services/TopupInterface?wsdl";
        String username = "IMEDIA_TEST";
        String password = "24112536637251";
        // Chuỗi Private Key dưới dạng PEM
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCyVWVR+TP7TGIV\n" +
                "k69V3/oVGJX5ZHdPCfZbQ6L2Eoex+6iJg+M67y3oMpYHzxUiN9VKMOIvn0sn8+h2\n" +
                "6azt9lHQRD4HJinSBCqoRs9G/9lLLcDGtbdXtLcDfPT6+5ntIb+CKNRXQHWirSM3\n" +
                "0yVXqbwLf5QWgzOH6X4CC2R5kM2McAReIeRhxGN80j50vTmRZQ9U9zY8EMn9OK2/\n" +
                "DNnZ2/Z0uXHsoAVtmcx47u9+XFk2Ze9xE1dKSP/ooRtWtAKwm5Q7v46KOBtDzXc2\n" +
                "6OqbkkeWwCLRtXy7ma6SVgKAT9A4GKjq6/r6GIKeer0dwlmxZ3Vq5LSLABBMD8bZ\n" +
                "N4pxryi/AgMBAAECggEAVWyEkUS/OrQ3E8Dfr1IPuEVDqegPIqRSBxuaFyd/Kqmy\n" +
                "7NRpZ5Skt+Jrbagrpm16VQSfhFJYrPWwOC3tMTvZqtrVn5UPWVO3n030Aj2SN+nd\n" +
                "uxHWlkOxmxEjRIp7DFUrKE0okfcFonwvL5GMBLcApb1iEXqdl5AoVRBh+716SLed\n" +
                "/dE3ShM1L9IsaQxuMN0TGwNUZkZYzfB2umxMvlHcmCESCHuMHO/RncamTB7QdhEK\n" +
                "r3mtd8xZecuFimLh1ClRLJDZebeLb8ybrt3krWPArgLYZCDPZKnDSnxG35yzJA37\n" +
                "cdK71ML5W/IvcGBethga2tMV5ke+Emfi4EpsybyyqQKBgQDtOFHyOH3f9p6X5QIx\n" +
                "macsFuotAWhGfUEocLboNO720JTi4Mw4N+YImNz9ksSNFXGXBMJCEvAxAyWVAeB1\n" +
                "d6M2CZJlybnXd0NkvP8mQvGGCx2IFMuRtzynIGDAOivs0wWC1PxQYEyDzGE5/QSv\n" +
                "rwCC1YKJKNTbsakPjhqx3wbnwwKBgQDAc6OPIvOWLpGO3sqjIfbr+a/Iq/YlRz+1\n" +
                "bX6aEOe5JN4C4xs1EnEg8q9g9NK5/mH2ctI6pG8AxO/sBWFHPzDm0g6UK0ESIley\n" +
                "XpDYB4BsEeoA6pYkBcfyDarHe03WhUS7u9/ZYVGwwa4J2uGZ4CmAoWuXIvC1hPJU\n" +
                "fWRakLynVQKBgCW7J42XXq4Y0/DlBAxPnD9vBaBS8PsFQS/lfbJBeSDY3FWZ2+G1\n" +
                "QmlrpvrONWUbXA3hO+S2jm29SmUA/2qvtM4Lh7WY+G5FEfsb9JlpXHEto2zZoedz\n" +
                "dbo/dCQfHI12oxHEPr2qE4GDKJPIos4uz63/t9uJGxI2l+VZfPV9u+NxAoGBALVi\n" +
                "SB14C6zYZ0gIo2PtdxQhWJQBvxSTenA8qr36gOv222hNNC9pGka7dKAlHxc9sobc\n" +
                "4Vdz80r+UkJZL74+yJBEGol72vCEfbMXfdyd9WPl3m7OqoN5D2ILj5JDnLE7GfT2\n" +
                "tZvkJWI6qRWQvmCQ7YzWltjzjXsHun33UMYq9COhAoGBAMQOLtARpd6zh7vgepnd\n" +
                "v46DLRXUuAGHl0jDssSPJabDeMufgsqlGa8Vyy4+4X1ZIhqM+cD1k6uBjiodlUAE\n" +
                "DXCntABHCGckX5298IljOQTUq5UpnsAm98n9+LkwTPU+aQ2OUT/fT/jluXVlNSoz\n" +
                "c5DZy1yl2g4BJPashtqNjnCW\n" +
                "-----END PRIVATE KEY-----";
        // Chuyển PEM thành đối tượng PrivateKey
        PrivateKey privateKey = base.getPrivateKeyFromPEM(privateKeyPEM);
        login login = new login(username,  password,  privateKey, url);
        String response = login.requestHandle(1400, login.username,  login.password,  login.privateKey, login.url);
        String jsonString = login.decodeJson(response);
        // In chuỗi phản hồi ra console
        System.out.println("======================================================================");
        System.out.println("Response: \n" + response);
        System.out.println("======================================================================");
        System.out.println("Token: " + login.getInfo(jsonString,"token"));
        System.out.println("ErrorCode: " + login.getInfo(jsonString,"errorCode"));

    }

    }

