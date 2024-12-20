package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class readFile {
    public static String[][] readCSV(String filePath) throws IOException {
        List<String[]> dataList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        boolean isFirstLine = true;
        while ((line = br.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false; // Bỏ qua dòng đầu tiên
                continue;
            }
            String[] dataRow = line.split(";"); // Dựa vào dấu phẩy để tách cột
            dataList.add(dataRow);
        }
        br.close();
        // Chuyển List<String[]> thành String[][]
        return dataList.toArray(new String[0][0]);
    }

    public static void main(String[] args) throws Exception{

            String[][] data = readCSV("src/main/java/config/dataTest.csv");
            // In ra toàn bộ dữ liệu
            for (String[] row : data) {
                for (String value : row) {
                    System.out.print(value + " ");
                }
                System.out.println("\nUsername: " + row[0]);
                System.out.println("Password: " + row[1]);
                System.out.println("Phone: " + row[2]);
                System.out.println("ProviderCode: " + row[3]);
                System.out.println("Amount: " + row[4]);
                System.out.println("ErrorCode: " + row[5]);
            }

    }
}
