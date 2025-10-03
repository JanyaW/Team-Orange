import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChargesByAge {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Integer> ages = new ArrayList<>();
        List<Double> charges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

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
                    ages.add(age);
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

        List<Double> charges50Plus = new ArrayList<>();
        List<Double> charges20OrLess = new ArrayList<>();

        for (int i = 0; i < ages.size(); i++) {
            int age = ages.get(i);
            double charge = charges.get(i);
            if (age >= 50) charges50Plus.add(charge);
            else if (age <= 20) charges20OrLess.add(charge);
        }

        double avg50Plus = charges50Plus.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avg20OrLess = charges20OrLess.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.printf("Average charges 50+: %.2f%n", avg50Plus);
        System.out.printf("Average charges 20-: %.2f%n", avg20OrLess);
    }
}
