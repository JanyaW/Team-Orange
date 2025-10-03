import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChargesByAge {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Double> chargesUnder20 = new ArrayList<>();
        List<Double> chargesOver50 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }
            System.out.println("Header: " + headerLine);

            String[] headers = headerLine.split(",");
            int ageIndex = -1, chargesIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                String col = headers[i].trim().toLowerCase();
                if (col.equals("age")) ageIndex = i;
                else if (col.equals("charges")) chargesIndex = i;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                try {
                    int age = Integer.parseInt(parts[ageIndex].trim());
                    double charge = Double.parseDouble(parts[chargesIndex].trim());

                    if (age <= 20) {
                        chargesUnder20.add(charge);
                    } else if (age >= 50) {
                        chargesOver50.add(charge);
                    }
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("Count of charges for <=20: " + chargesUnder20.size());
        System.out.println("Count of charges for >=50: " + chargesOver50.size());
    }
}
