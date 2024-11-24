package billing;

import java.security.PrivateKey;

public class payBill extends getBill{
    int prCodeBill;

    String referenceCode;
    long amount;
    String phone;
 public payBill(int prCodeBill, String username, String password, String serviceCode, String billingCode, String requestID,String referenceCode, long amount, String phone, PrivateKey privateKey, String url, String typeService){
     super(0,username, password,serviceCode,billingCode,requestID,privateKey,url,typeService);
     this.prCodeBill =prCodeBill;
     this.referenceCode = referenceCode;
     this.amount = amount;
     this.phone = phone;

 }
    public  String pay_bill(int prCode, String username, String password, String serviceCode, String billingCode, String requestID,String referenceCode, long amount, String phone, PrivateKey privateKey, String url) throws Exception {
        String data = "pay_bill#"+username +"#" + password+"#"+requestID+"#"+billingCode+"#"+serviceCode+"#"+referenceCode+"#"+ amount;
        System.out.println("Data payBill:" + data);
        String signature = sign(data, privateKey);

        String request = " {\n" +
                "          \"pr_code\": \""+prCode+"\",\n" +
                "          \"message\": {\n" +
                "              \"username\": \""+username+"\",\n" +
                "              \"password\": \""+password+"\",\n" +
                "              \"service_code\": \""+serviceCode+"\",\n" +
                "              \"billing_code\": \""+billingCode+"\",\n" +
                "              \"partner_trans_id\": \""+requestID+"\",\n" +
                "              \"reference_code\": \""+referenceCode+"\",\n" +
                "              \"authkey\": \""+signature+"\",\n" +
                "              \"amount\": "+amount+",\n" +
                "              \"contact_id\": \""+phone+"\"\n" +
                "          }\n" +
                "      }";

        System.out.println("Request PayBill: "+ request);
        return sendRequest(url, request);

    }

    public static void main(String[] args) throws Exception {
     baseRequestBill base =  new baseRequestBill();
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
        String serviceCode = "TPB";
        String billCode ="0355273394";
        String requestID = base.createRequestID("HangPTDV_payBill");


        String phone = "0355273394";

        // Chuyển PEM thành đối tượng PrivateKey
        PrivateKey privateKey = base.getPrivateKeyFromPEM(privateKeyPEM);

        getBill getbill = new getBill(1009,username,password,serviceCode,billCode,requestID,privateKey,url,"TC");
        String responseGetBill = getbill.queryBill(getbill.prCode,getbill.username,getbill.password,getbill.serviceCode,getbill.billCode,getbill.requestID,getbill.privateKey,getbill.url);
        System.out.println("Response GetBill: "+ base.formatJSON(responseGetBill));
        System.out.println("======================================================================");
        String referCode = getbill.getInf(responseGetBill,"data","reference_code");
        long amount = Long.parseLong(getbill.getInf(responseGetBill,"data","amount"));
        long minAmount = Long.parseLong(getbill.getInf(responseGetBill,"data","minAmount"));

        payBill paybill = new payBill(1010,getbill.username,getbill.password,getbill.serviceCode,getbill.billCode,requestID,referCode,amount,phone,getbill.privateKey,getbill.url,getbill.typeService);
       if(paybill.typeService.equals("HD")){
           paybill.amount = amount;
           System.out.println("Thanh toan giao dich "+ paybill.typeService +": "+ referCode + " voi so tien "+ paybill.amount);
           String responsePayBill = paybill.pay_bill(paybill.prCodeBill, paybill.username,paybill.password,paybill.serviceCode,paybill.billCode,paybill.requestID,paybill.referenceCode,paybill.amount,paybill.phone,paybill.privateKey,paybill.url);
           System.out.println("Response PayBill: "+ base.formatJSON(responsePayBill));
       } else if (paybill.typeService.equals("TC")){
           paybill.amount = minAmount;
           System.out.println("Thanh toan giao dich: "+ paybill.typeService+": "+ referCode + " voi so tien "+ paybill.amount);
           String responsePayBill = paybill.pay_bill(paybill.prCodeBill, paybill.username,paybill.password,paybill.serviceCode,paybill.billCode,paybill.requestID,paybill.referenceCode,paybill.amount,paybill.phone,paybill.privateKey,paybill.url);
           System.out.println("Response PayBill: "+ base.formatJSON(responsePayBill));
       }

    }

}
