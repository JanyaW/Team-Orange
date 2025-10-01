import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmokerHistogram {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<String> smokers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

            String[] headers = headerLine.split(",");
            int smokerIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("smoker")) {
                    smokerIndex = i;
                    break;
                }
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                smokers.add(parts[smokerIndex].trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        int smokerCount = 0;
        int nonSmokerCount = 0;
        for (String s : smokers) {
            if (s.equalsIgnoreCase("yes")) smokerCount++;
            else if (s.equalsIgnoreCase("no")) nonSmokerCount++;
        }

        System.out.println("Smokers: " + smokerCount);
        System.out.println("Non-smokers: " + nonSmokerCount);
    }
}
