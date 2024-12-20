package billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class baseRequestBill {

    // Hàm chuyển chuỗi PEM thành đối tượng PrivateKey
    public PrivateKey getPrivateKeyFromPEM(String pem) throws Exception {
        // Loại bỏ header/footer
        String privateKeyPEM = pem.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Decode từ Base64
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);

        // Tạo đối tượng PrivateKey
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    // Hàm ký chuỗi
    public  String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }
    public  String createRequestID(String partnerName) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssss");
        String formattedDate = dateFormat.format(new Date());
        // Sinh số ngẫu nhiên 6 chữ số
        Random random = new Random();
        int subResult = random.nextInt(9000) + 1000;
        return partnerName+ "_" + formattedDate+ "_"+ subResult;

    }
    public String sendRequest(String url, String request) throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(request);
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");
//        post.setHeader("SoapAction","''");
        return EntityUtils.toString(client.execute(post).getEntity());
    }

    public String getInfo(String decodedJson,String info) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(decodedJson);
        return rootNode.get(info).asText();

    }
 public String getInf(String jsonString,String obj,String info)  {
        String inf ="";
     try {
     ObjectMapper mapper = new ObjectMapper();
     JsonNode rootNode = null;

         rootNode = mapper.readTree(jsonString);


     // Truy cập đối tượng lồng nhau
     JsonNode personNode = rootNode.get(obj);
    inf= personNode.get(info).asText();

     } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
     }
     return inf;
 }

    public  String formatJSON(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString); // Parse chuỗi JSON
        return jsonObject.toString(4); // Số `4` là số khoảng trắng thụt lề
    }
    public static void main(String[] args) throws Exception {

        // Chuỗi Private Key dưới dạng PEM
        String privateKeyPEM = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO+N1hxtxzObXrat\n" +
                "KKtplV42JES0WLF1x9gQWxlxfHZvlnswHDuyGJcTGEs1FcKBmsMvB84exuMxC93V\n" +
                "eyq82J4ADZa0IKsQ8pW19+JMz0Z+vVPVgwR2h+KJDPpXCjKw5HSZkbgm2oJ6sBCE\n" +
                "KTy49BjI0Rn0V4Xi52K0MkXX696zAgMBAAECgYEA6XI1Z3rrlzUf9bGFYpYAA9GL\n" +
                "QpDlpfp7h+lYfdEEU36nDOFzghEquX7YO+I9lFEs+mzIlGuVsi1HvSSfZKSoCl6S\n" +
                "msbVUWkZEYPLhSG5ikpsb5DZnMsT5W/yl8N9HcZsO9C1Cyto0EgvZxnNOjdf4Rsw\n" +
                "Zhq63C7cI4lUrO4g4NECQQD8DOMO/5+vYiHZhgiLUdnvEyqYBRXcZ4TJdZReF6yU\n" +
                "Qd8nG70My1zmk5tptELqYCwup2VT2ziNEyqHXLNwp8SPAkEA807R5QXBHKh36gVh\n" +
                "OfrCKBOvou59N8gho6D0m1Ty6uIFB2rUe638Dj15MfHehf5UPzbnbskfj4AdHuBO\n" +
                "KUn9nQJAcBrXPsuJXbta7OIFmNnOAdzXfAf/Ain00JoAZJ1JACQQOdfHjRJCfre2\n" +
                "TxyDCrW90P5ZPiPqEi0tJEmh8gBclwJAN8Ipce3WqqWlDXl8JZhk5GBWkOVMxvrT\n" +
                "UrdxNyPJo7B2bJO77DgcGntWCe8fCuAVGIORmB75X56BjfDjmKy/NQJBAMtS+ib2\n" +
                "pqdRjcVPUuN3Qjtzw9vD9nx5FfhJDOUqqee3Q3uEp+jFPuUk8kSXrL+3wTz55Rly\n" +
                "3PFIBeCC9S9PoXk=\n" +
                "-----END RSA PRIVATE KEY-----";
        baseRequestBill obj = new baseRequestBill();
        // Chuyển PEM thành đối tượng PrivateKey
        PrivateKey privateKey = obj.getPrivateKeyFromPEM(privateKeyPEM);

        // Chuỗi cần ký
        String data = "Chuỗi cần ký";

        // Ký chuỗi
        String signedData = obj.sign(data, privateKey);
        System.out.println("Chuỗi đã ký (Base64): \n" + signedData);

        System.out.println(obj.createRequestID("Hangptdv"));


    }
}
