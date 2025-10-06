import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelloWorld {
    public static void main(String[] args) {
        // Use provided filename arg if present; otherwise default to insurance.csv
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        // ========= 1) START OF DRIVER =========
        System.out.println("=======================================");
        System.out.println("          INSURANCE DATA DRIVER        ");
        System.out.println("=======================================\n");

        // ========= 2) BMI HISTOGRAM SECTION =========
        System.out.println(">>> SECTION 1: BMI VERTICAL HISTOGRAM <<<");

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

            if (bmiIndex == -1) {
                System.err.println("No 'bmi' column found in header for file: " + filename);
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length <= bmiIndex) continue; // guard short rows
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

        if (bmis.isEmpty()) {
            System.err.println("No BMI values parsed.");
            return;
        }

        double min = Collections.min(bmis);
        double max = Collections.max(bmis);
        int numBins = 10;
        double range = max - min;
        double binWidth = (range == 0.0) ? 1.0 : (range / numBins); // avoid divide-by-zero

        int[] counts = new int[numBins];
        for (double bmi : bmis) {
            int bin = (int) ((bmi - min) / binWidth);
            if (bin >= numBins) bin = numBins - 1; // include max value
            if (bin < 0) bin = 0;
            counts[bin]++;
        }

        int maxCount = 0;
        for (int count : counts) {
            if (count > maxCount) maxCount = count;
        }

        System.out.println("\nBMI Vertical Histogram\n");
        for (int level = maxCount; level > 0; level--) {
            for (int count : counts) {
                System.out.printf("%-10s", (count >= level) ? "*" : " ");
            }
            System.out.println();
        }

        for (int i = 0; i < numBins; i++) System.out.printf("%-10s", "——");
        System.out.println();

        for (int i = 0; i < numBins; i++) {
            double lower = min + i * binWidth;
            double upper = lower + binWidth;
            System.out.printf("%-10s", String.format("[%.1f-%.1f]", lower, upper));
        }
        System.out.println("\n");

        System.out.println(">>> END OF SECTION 1 <<<\n");

        // ========= 3) CHARGES BY AGE SECTION =========
        System.out.println(">>> SECTION 2: CHARGES BY AGE <<<");
        try {
            ChargesByAge.main(new String[]{ filename });
        } catch (Throwable t) {
            System.err.println("Warning: Could not run ChargesByAge: " + t.getMessage());
        }
        System.out.println(">>> END OF SECTION 2 <<<\n");

        // ========= 4) CHARGES PER CHILD SECTION =========
        System.out.println(">>> SECTION 3: CHARGES PER CHILD <<<");
        try {
            ChargesPerChild.main(new String[]{ filename });
        } catch (Throwable t) {
            System.err.println("Warning: Could not run ChargesPerChild: " + t.getMessage());
        }
        System.out.println(">>> END OF SECTION 3 <<<\n");

        // ========= 5) SOUTH SMOKER CHARGES SECTION =========
        System.out.println(">>> SECTION 4: SOUTH SMOKER CHARGES (>=25%?) <<<");
        try {
            SouthSmokerCharges.main(new String[]{ filename });
        } catch (Throwable t) {
            System.err.println("Warning: Could not run SouthSmokerCharges: " + t.getMessage());
        }
        System.out.println(">>> END OF SECTION 4 <<<\n");

        // ========= 6) SMOKER AGE HISTOGRAM SECTION =========
        System.out.println(">>> SECTION 5: SMOKER AGE HISTOGRAM <<<");
        try {
            SmokerAgeHistogram.main(new String[]{ filename });
        } catch (Throwable t) {
            System.err.println("Warning: Could not run SmokerAgeHistogram: " + t.getMessage());
        }
        System.out.println(">>> END OF SECTION 5 <<<\n");

        // ========= 7) SMOKER AGE SPLIT (Young > Old cutoff?) =========
        System.out.println(">>> SECTION 6: SMOKER AGE SPLIT (young vs. old) <<<");
        try {
            SmokerAgeSplit.main(new String[]{ filename });
        } catch (Throwable t) {
            System.err.println("Warning: Could not run SmokerAgeSplit: " + t.getMessage());
        }
        System.out.println(">>> END OF SECTION 6 <<<\n");

        // ========= 8) FUTURE FEATURES PLACEHOLDER =========
        System.out.println(">>> ADDITIONAL SECTIONS COMING SOON <<<");
        System.out.println("   AgeHistogramHorizontal.main(new String[]{ filename });");
        System.out.println("   RegionFairness.main(new String[]{ filename });");
        System.out.println("   SmokersVsNonSmokersHistogram.main(new String[]{ filename });");
        System.out.println(">>> END OF DRIVER <<<");
    }
}
