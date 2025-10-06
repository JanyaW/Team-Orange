import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetInfo {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Double> bmis = new ArrayList<>();
        List<Double> ages = new ArrayList<>();
        // List<Double> bmis = new ArrayList<>();
        List<Double> children = new ArrayList<>();
        List<Double> charges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

            String[] headers = headerLine.split(",");
            int bmiIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("bmi")) {
                    bmiIndex = i;
                    break;
                }
            }
            int ageIndex = -1, childrenIndex = -1, chargesIndex = -1;
            bmiIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                String col = headers[i].trim().toLowerCase();
                if (col.equals("age")) ageIndex = i;
                else if (col.equals("bmi")) bmiIndex = i;
                else if (col.equals("children")) childrenIndex = i;
                else if (col.equals("charges")) chargesIndex = i;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    bmis.add(Double.parseDouble(parts[bmiIndex].trim()));
                    ages.add(Double.parseDouble(parts[ageIndex].trim()));
                    bmis.add(Double.parseDouble(parts[bmiIndex].trim()));
                    children.add(Double.parseDouble(parts[childrenIndex].trim()));
                    charges.add(Double.parseDouble(parts[chargesIndex].trim()));
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // ✅ Preview the first 10 BMI values
        System.out.println("First 10 BMI values:");
        for (int i = 0; i < Math.min(10, bmis.size()); i++) {
            System.out.println(bmis.get(i));
        }
        printStats("Age", ages);
        System.out.println();
        printStats("BMI", bmis);
        System.out.println();
        printStats("Children", children);
        System.out.println();
        printStats("Charges", charges);
    }

    private static void printStats(String name, List<Double> values) {
        if (values.isEmpty()) {
            System.out.println(name + ": No data");
            return;
        }

        int count = values.size();
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        double variance = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).sum() / count;
        double std = Math.sqrt(variance);

        Collections.sort(values);
        double min = values.get(0);
        double max = values.get(values.size() - 1);
        double p25 = percentile(values, 25);
        double p50 = percentile(values, 50);
        double p75 = percentile(values, 75);

        System.out.printf(
            "%s -> Count: %d, Mean: %.2f, Std: %.2f, Min: %.2f, 25%%: %.2f, 50%%: %.2f, 75%%: %.2f, Max: %.2f%n",
            name, count, mean, std, min, p25, p50, p75, max
        );
    }

    private static double percentile(List<Double> sorted, double percentile) {
        if (sorted.isEmpty()) return Double.NaN;
        int index = (int) Math.ceil(percentile / 100.0 * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }
}