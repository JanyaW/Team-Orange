import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChargesVsBmi {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Double> bmis = new ArrayList<>();
        List<Double> charges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

            String[] headers = headerLine.split(",");
            int bmiIndex = -1, chargesIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                String col = headers[i].trim().toLowerCase();
                if (col.equals("bmi")) bmiIndex = i;
                else if (col.equals("charges")) chargesIndex = i;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    bmis.add(Double.parseDouble(parts[bmiIndex].trim()));
                    charges.add(Double.parseDouble(parts[chargesIndex].trim()));
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.printf("Read %d rows of BMI/charges data.%n", bmis.size());

        double meanX = bmis.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double meanY = charges.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        double numerator = 0.0;
        double denominator = 0.0;
        for (int i = 0; i < bmis.size(); i++) {
            numerator += (bmis.get(i) - meanX) * (charges.get(i) - meanY);
            denominator += Math.pow(bmis.get(i) - meanX, 2);
        }
        double slope = numerator / denominator;
        double intercept = meanY - slope * meanX;

        System.out.printf("Regression line: y = %.2f + %.2f*x%n", intercept, slope);
    }
}
