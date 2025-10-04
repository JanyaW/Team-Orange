import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SouthSmokerCharges {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<String> smokers = new ArrayList<>();
        List<String> regions = new ArrayList<>();
        List<Double> charges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

            String[] headers = headerLine.split(",");
            int smokerIndex = -1, regionIndex = -1, chargesIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                String col = headers[i].trim().toLowerCase();
                if (col.equals("smoker")) smokerIndex = i;
                else if (col.equals("region")) regionIndex = i;
                else if (col.equals("charges")) chargesIndex = i;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    String smoker = parts[smokerIndex].trim().toLowerCase();
                    String region = parts[regionIndex].trim().toLowerCase();
                    double charge = Double.parseDouble(parts[chargesIndex].trim());

                    smokers.add(smoker);
                    regions.add(region);
                    charges.add(charge);
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        List<Double> southSmokerCharges = new ArrayList<>();
        List<Double> otherSmokerCharges = new ArrayList<>();

        for (int i = 0; i < smokers.size(); i++) {
            if (!smokers.get(i).equals("yes")) continue; 
            if (regions.get(i).contains("south")) {
                southSmokerCharges.add(charges.get(i));
            } else {
                otherSmokerCharges.add(charges.get(i));
            }
        }

        double avgSouth = southSmokerCharges.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double avgOther = otherSmokerCharges.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        System.out.printf("Average charges (South smokers): %.2f%n", avgSouth);
        System.out.printf("Average charges (Other smokers): %.2f%n", avgOther);
    }
}
