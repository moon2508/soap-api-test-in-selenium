package itopup;
import config.readFile;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.security.PrivateKey;

public class testTopup {
    baseRequest base = new baseRequest();
    String url = "https://haloship.imediatech.com.vn:8087/ItopupService2.0_IMD/services/TopupInterface";
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
    @DataProvider(name = "csvData")
    public Object[][] provideData() throws Exception {
        String[][] data = readFile.readCSV("src/main/java/config/dataTest.csv");
        return java.util.Arrays.copyOfRange(data, 0, data.length); // Bỏ qua dòng đầu tiên
    }

    @Test(dataProvider = "csvData")
    public void exportResultTesting(String ID,String username, String password, String phone, String providerCode, String amount, String errorCode) throws Exception {
        Reporter.setEscapeHtml(true);
        Reporter.log("=======================BEGIN TESTING==============================================");
        long productAmount = Integer.parseInt(amount);
        PrivateKey privateKey = base.getPrivateKeyFromPEM(privateKeyPEM);
        String requestID = base.createRequestID("HangPTDV_TOPUP"); login login = new login(username,  password,  privateKey, url);
        String resLogin = login.requestHandle(1400, username,  password,  privateKey, url);
        Reporter.log("======================================================================");
//        System.out.println("Response Login: \n"+ resLogin);

        //ham login
        String jsonLogin = login.decodeJson(resLogin);
        String token = login.getInfo(jsonLogin,"token");
        topup topup= new topup(login.username,requestID,  token, phone,  providerCode,productAmount,0,0,"", login.privateKey, login.url);
        String response = topup.requestHandle(1200,topup.username,topup.requestID,  topup.token, topup.phone,  topup.provider,topup.amount,0,0,topup.key, topup.privateKey, topup.url);
        String jsonString = topup.decodeJson(response);
        Reporter.log("======================================================================");
//        System.out.println("Response Topup: \n" + response);
        Reporter.log("======================================================================");
        String errorCodeActual = topup.getInfo(jsonString, "errorCode");
        Assert.assertEquals(errorCodeActual,errorCode,"Fail testing with ID_TESTCASE " + ID + "\n Actual errorCode: " + errorCodeActual +"\n Expect errorCode: " + errorCode);
        Reporter.log("===================================================END TESTING ===================================================");

    }
}
