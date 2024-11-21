package com.test;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

public class topup extends login {
    public  String requestHandle(int operation, String username, String requestID, String token, String phone, String provider, int amount, int productID,int quantity,String key, PrivateKey privateKey, String url) throws Exception {
        String data = username +"|" + requestID + "|"+ token + "|"+ operation;
        System.out.println("Data:" + data);

        String signature = sign(data, privateKey);
        String requestBody ="{\n" +
                "\"operation\": "+operation+ ",\n" +
                "\"username\": \""+username+"\",\n" +
                "\"requestID\": \""+requestID+"\",\n" +
                "  \"buyItems\": [\n" +
                "    {\n" +
                "      \"productId\":"+productID +",\n" +
                "      \"quantity\": "+quantity+"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"keyBirthdayTime\": \""+key+"\",\n" +
                "\"targetAccount\": \""+phone+"\",\n" +
                "\"providerCode\": \""+provider+"\",\n" +
                "\"topupAmount\": "+amount+",\n" +
                "\"signature\": \""+signature+"\",\n" +
                "\"token\":\""+token +"\"\n" +
                "}";

        String request = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <Body>\n" +
                "        <requestHandle xmlns=\"http://interfaces.itopup.vnptepay.vn\">\n" +
                "            <requestData>"+ requestBody+"</requestData>\n" +
                "        </requestHandle>\n" +
                "    </Body>\n" +
                "</Envelope>";

        System.out.println("Request TOPUP: "+ request);
        return sendRequest(url, request);
    }

    public static String decrypt(String payload, String key) throws Exception {
        // Giải mã chuỗi base64 thành byte[]
        byte[] data = Base64.getDecoder().decode(payload);

        // Lấy IV từ khóa (8 byte đầu của khóa)
        byte[] iv = key.substring(0, 8).getBytes("UTF-8");

        // Đảm bảo khóa có độ dài 24 byte cho TripleDES
        byte[] keyBytes = key.getBytes("UTF-8");
        if (keyBytes.length < 24) {
            byte[] paddedKey = new byte[24];
            System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
            keyBytes = paddedKey;
        }

        // Tạo khóa TripleDES và IV
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Khởi tạo Cipher với chế độ CBC và padding PKCS5
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // Giải mã dữ liệu
        byte[] decryptedBytes = cipher.doFinal(data);

        // Chuyển kết quả giải mã từ byte[] sang String
        return new String(decryptedBytes, "UTF-8");
    }
    public ArrayList  getSoftpinCode(String data, String secretKey,String iv) throws  Exception {
        ArrayList<String> softpinPinCodes = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(data);

        // Lấy danh sách "products"
        JSONArray productsArray = jsonObject.getJSONArray("products");

        for (Object productObj : productsArray) {
            JSONObject product = (JSONObject) productObj;

            // Lấy mảng "softpins" từ product
            JSONArray softpinsArray = product.getJSONArray("softpins");

            // Duyệt qua từng softpin trong mảng "softpins" bằng for-each
            for (Object softpinObj : softpinsArray) {
                JSONObject softpin = (JSONObject) softpinObj;
                String softpinPinCode = softpin.getString("softpinPinCode");
                softpinPinCodes.add(softpinPinCode);

                // In ra mã softpinPinCode
//                System.out.println("Softpin Code: " + softpinPinCode);
                System.out.println("Softpin Code Decrypt: " + decrypt(softpinPinCode,secretKey));

        }
        }
        return softpinPinCodes;
    }


    public static void main(String[] args) throws Exception {


    String url = "https://haloship.imediatech.com.vn:8087/ItopupService2.0_IMD/services/TopupInterface";
    String username = "IMEDIA_TEST";
    String password = "24112536637251";
    String keyBirthdayTime = "2022/11/29 09:26:01.690";
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
        topup topup= new topup();
        login login = new login();

    // Chuyển PEM thành đối tượng PrivateKey
    PrivateKey privateKey = topup.getPrivateKeyFromPEM(privateKeyPEM);
    int operation = 1200;
    String requestID = topup.createRequestID("HangPTDV_TOPUP");
    String phone = "0982345678";
    String provider = "Viettel";
    String softpinKey = "70cf4fe7b75b72ddd78cbdb6";
    int productID = 1;
    int quantity = 1;
    int amount = 50000;

        String resLogin = login.requestHandle(1400, username,  password,  privateKey, url);
        System.out.println("======================================================================");
        System.out.println("Response Login: \n"+ resLogin);

        //ham login
        String jsonLogin = login.decodeJson(resLogin);
        String token = login.getInfo(jsonLogin,"token");

        // ham topup transaction
        // String response = topup.requestHandle(operation,username,requestID,  token, phone,  provider,amount,0,0,keyBirthdayTime, privateKey, url);

        //ham check transaction
        //String response = topup.requestHandle(1300,username,"HangPTDV_TOPUP_2011202415460017_7896",  token, "",  "",0,0,0,keyBirthdayTime, privateKey, url);

        // ham download transaction
        String response = topup.requestHandle(1000,username,requestID,  token, phone,  provider,amount,555,2,keyBirthdayTime, privateKey, url);

        //ham redownload transaction
        //String response = topup.requestHandle(1100,username,"HangPTDV_TOPUP_2011202416060013_4890",  token, phone,  provider,amount,productID,2,keyBirthdayTime, privateKey, url);

        String jsonString = topup.decodeJson(response);

        System.out.println("======================================================================");
        System.out.println("Response Topup: \n" + response);
        System.out.println("======================================================================");
        System.out.println("ErrorCode: " + topup.getInfo(jsonString, "errorCode"));
        System.out.println("Softpin Code: "+ topup.getSoftpinCode(jsonString,softpinKey,"12345678"));


}
}
