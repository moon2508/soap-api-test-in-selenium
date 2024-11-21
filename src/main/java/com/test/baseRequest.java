package com.test;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class baseRequest {

    // Hàm chuyển chuỗi PEM thành đối tượng PrivateKey
    public PrivateKey getPrivateKeyFromPEM(String pem) throws Exception {
        // Loại bỏ header/footer
        String privateKeyPEM = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
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
    public  String createRequestID(String partnerName) {
        String requestID = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssss");
            String formattedDate = dateFormat.format(new Date());
            // Sinh số ngẫu nhiên 6 chữ số
            Random random = new Random();
            int subResult = random.nextInt(9000) + 1000;
            return partnerName+ "_" + formattedDate+ "_"+ subResult;
        } catch (Exception e) {
            requestID = "";
            e.printStackTrace();
        }
        return requestID;
    }

    public String decodeJson(String xmlResponse){
        String JsonResponse = null;
        try {
            // Bước 1: Parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes("UTF-8")));

            // Bước 2: Lấy nội dung từ thẻ <requestHandleReturn>
            Node requestHandleReturnNode = document.getElementsByTagName("requestHandleReturn").item(0);
            String encodedJson = requestHandleReturnNode.getTextContent();

            // Bước 3: Giải mã HTML entities (ví dụ: &quot; -> ")
            JsonResponse = encodedJson
                    .replace("&quot;", "\"")
                    .replace("&amp;", "&");

        } catch (Exception e) {
            JsonResponse = "";
            e.printStackTrace();
        }
        return JsonResponse;
    }
    public static void main(String[] args) throws Exception {
        try {
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
            baseRequest obj = new baseRequest();
            // Chuyển PEM thành đối tượng PrivateKey
            PrivateKey privateKey = obj.getPrivateKeyFromPEM(privateKeyPEM);

            // Chuỗi cần ký
            String data = "Chuỗi cần ký";

            // Ký chuỗi
            String signedData = obj.sign(data, privateKey);
            System.out.println("Chuỗi đã ký (Base64): \n" + signedData);

            System.out.println(obj.createRequestID("Hangptdv"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
