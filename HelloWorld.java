import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HelloWorld {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Double> bmis = new ArrayList<>();

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

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    bmis.add(Double.parseDouble(parts[bmiIndex].trim()));
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        double min = bmis.stream().min(Double::compare).orElse(0.0);
        double max = bmis.stream().max(Double::compare).orElse(0.0);
        int numBins = 10;
        double binWidth = (max - min) / numBins;

        int[] counts = new int[numBins];
        for (double bmi : bmis) {
            int bin = (int) ((bmi - min) / binWidth);
            if (bin == numBins) bin--; 
            counts[bin]++;
        }

        int maxCount = 0;
        for (int count : counts) {
            if (count > maxCount) maxCount = count;
        }

        for (int level = maxCount; level > 0; level--) {
            for (int count : counts) {
                if (count >= level) {
                    System.out.printf("%-10s", "*"); // wider column for full bin ranges
                } else {
                    System.out.printf("%-10s", " ");
                }
            }
            System.out.println();
        }

        for (int i = 0; i < numBins; i++) {
            double lower = min + i * binWidth;
            double upper = lower + binWidth;
            System.out.printf("%-10s", String.format("[%.1f-%.1f]", lower, upper));
        }
        System.out.println();

         // === Problem 3: Horizontal Histogram of Age ===
        System.out.println("=== Horizontal Histogram of Age ===");

        List<InsuranceRecord> allRecords = getFirstNRecords(filename, Integer.MAX_VALUE);

        List<Integer> ages = new ArrayList<>();
        for (InsuranceRecord r : allRecords) {
            ages.add(r.age);
        }

        int minAge = Collections.min(ages);
        int maxAge = Collections.max(ages);
        int ageBins = 8;
        int ageBinWidth = (int) Math.ceil((maxAge - minAge + 1) / (double) ageBins);

        int[] ageCounts = new int[ageBins];
        for (int age : ages) {
            int binIndex = (age - minAge) / ageBinWidth;
            if (binIndex >= ageBins) binIndex = ageBins - 1;
            ageCounts[binIndex]++;
        }

        for (int i = 0; i < ageBins; i++) {
            int binStart = minAge + i * ageBinWidth;
            int binEnd = binStart + ageBinWidth - 1;
            System.out.printf("%2d–%2d | ", binStart, binEnd);
            for (int j = 0; j < ageCounts[i]; j++) {
                System.out.print("*");
            }
            System.out.println(" (" + ageCounts[i] + ")");
        }
    }    
    // === Problem 1: Get First N Records ===
    public static List<InsuranceRecord> getFirstNRecords(String filename, int N) {
        List<InsuranceRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String header = br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null && records.size() < N) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 7) continue;
                try {
                    InsuranceRecord record = new InsuranceRecord(parts);
                    records.add(record);
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return records;
    };
    
    // === Problem 5: Count Records by Number of Children ===
    public static void countChildrenRecords(List<InsuranceRecord> records) {
        Map<Integer, Integer> childrenCount = new TreeMap<>(); // TreeMap keeps keys sorted

        for (InsuranceRecord record : records) {
            int numChildren = record.children;
            childrenCount.put(numChildren, childrenCount.getOrDefault(numChildren, 0) + 1);
        }

        System.out.println("=== Number of Records per Number of Children ===");
        for (Map.Entry<Integer, Integer> entry : childrenCount.entrySet()) {
            System.out.printf("Children: %d => %d record%s\n",
                    entry.getKey(),
                    entry.getValue(),
                    entry.getValue() == 1 ? "" : "s");
        }
    }   
    



    
}

    
    

