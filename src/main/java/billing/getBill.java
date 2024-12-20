package billing;
import java.security.PrivateKey;
public class getBill extends baseRequestBill{
    int prCode;
    String username;
    String password;
    String serviceCode;
    String billCode;
    String requestID;
    PrivateKey privateKey;
    String url;
    String typeService;
    public getBill(int prCode, String username, String password, String serviceCode, String billingCode, String requestID, PrivateKey privateKey, String url,  String typeService){
        this.prCode =prCode;
        this.username = username;
        this.password = password;
        this.serviceCode = serviceCode;
        this.billCode= billingCode;
        this.requestID = requestID;
        this.privateKey = privateKey;
        this.url = url;
        this.typeService = typeService;
    }
    public  String queryBill(int prCode, String username, String password,String serviceCode,String billingCode, String requestID, PrivateKey privateKey, String url) throws Exception {
        String data = "get_bill#"+username +"#" + password+"#"+requestID+"#"+billingCode+"#"+serviceCode;
        System.out.println("Data:" + data);
        String signature = sign(data, privateKey);

        String request = "{\n" +
                "    \"pr_code\": \""+prCode+"\",\n" +
                "    \"message\": {\n" +
                "        \"username\": \""+username+"\",\n" +
                "        \"password\": \""+password+"\",\n" +
                "        \"service_code\": \""+serviceCode+"\",\n" +
                "        \"billing_code\": \""+billingCode+"\",\n" +
                "        \"partner_trans_id\": \""+requestID+"\",\n" +
                "        \"authkey\": \""+signature+"\"\n" +
                "    }\n" +
                "}";

        System.out.println("Request: "+ request);
        return sendRequest(url, request);

    }

    public static void main(String[] args) throws Exception {
        baseRequestBill base = new baseRequestBill();
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
        String url = "http://222.252.17.162:8080/v1/sandbox/services/paybill";
        String username = "integrate_account";
        String password = "a1ec3b73f427c514ab64ce99c891b73f";
        String serviceCode = "EVN";
        String billCode ="0355273394";
        String requestID = base.createRequestID("HangPTDV_getBill");
        // Chuyển PEM thành đối tượng PrivateKey
        PrivateKey privateKey = base.getPrivateKeyFromPEM(privateKeyPEM);
        getBill bill = new getBill(1009,username,password,serviceCode,billCode,requestID,privateKey,url,"HD");

        String response = bill.queryBill(bill.prCode,bill.username,bill.password,bill.serviceCode,bill.billCode,bill.requestID,bill.privateKey,bill.url);
//        String status = bill.getInfo(response,"status");
        // In chuỗi phản hồi ra console
        System.out.println("======================================================================");
        System.out.println("Response: \n" + bill.formatJSON(response));
        System.out.println("======================================================================");
        System.out.println("Final status:"+ bill.getInf(response,"data","final_status"));
        if(bill.typeService.equals("HD")){
            System.out.println("So tien can thanh toan:"+ bill.getInf(response,"data","amount"));
        } else if(bill.typeService.equals("TC")){
            System.out.println("So tien can thanh toan:"+ bill.getInf(response,"data","minAmount"));
        }
    }
}
