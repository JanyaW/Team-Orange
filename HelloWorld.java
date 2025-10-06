import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    }    
}
