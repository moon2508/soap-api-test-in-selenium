package itopup;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SoapAPITesting {
    public static void main(String[] args) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost post = new HttpPost("https://haloship.imediatech.com.vn:8087/ItopupService1.4/services/TopupInterface?wsdl");
            String request = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "    <Body>\n" +
                    "        <signInAsPartner xmlns=\"http://interfaces.itopup.vnptepay.vn\">\n" +
                    "            <username>IMEDIA_TEST</username>\n" +
                    "            <password>24112536637251</password>\n" +
                    "        </signInAsPartner>\n" +
                    "    </Body>\n" +
                    "</Envelope>";
            System.out.println("Request: "+ request);

            StringEntity entity = new StringEntity(request);
            post.setEntity(entity);
            post.setHeader("Content-Type", "text/xml");
            post.setHeader("SoapAction","''");
            client.execute(post);
            // Gửi yêu cầu và nhận phản hồi
            String response = EntityUtils.toString(client.execute(post).getEntity());

            // In chuỗi phản hồi ra console
            System.out.println("Start test API");
            System.out.println("======================================================================");
            System.out.println("SOAP Response: \n" + response);
            System.out.println("======================================================================");
            System.out.println(" End test API");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
