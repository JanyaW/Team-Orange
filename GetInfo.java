import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetInfo {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }
            System.out.println("Header: " + headerLine);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}