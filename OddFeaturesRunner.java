import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OddFeaturesRunner {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";
        List<InsuranceRecord> records = loadAll(filename);
        if (records.isEmpty()) {
            System.err.println("No records loaded.");
            return;
        }

        // Problem 3: Horizontal text histogram of AGE
        System.out.println("=== Problem 3: Horizontal Age Histogram ===");
        printAgeHistogramHorizontal(records, 8);

        // Problem 5: Total number of records for each number of children
        System.out.println("\n=== Problem 5: Count Records by Children ===");
        countChildrenRecords(records);

        // Problem 7: Region fairness (<= 5% difference?)
        System.out.println("\n=== Problem 7: Region Fairness (<=5% difference?) ===");
        checkRegionFairness(records);

        // Problem 9: Charge ranges by BMI groups
        System.out.println("\n=== Problem 9: Charge Ranges by BMI Groups ===");
        compareChargeRangesByBMI(records);

        // Problem 11: Smokers vs Non-Smokers (vertical text histogram)
        System.out.println("\n=== Problem 11: Smokers vs Non-Smokers (Vertical Histogram) ===");
        smokersVsNonSmokersHistogram(records);

        // Problem 13: Do smokers average lower BMI than non-smokers?
        System.out.println("\n=== Problem 13: Do Smokers Avg Lower BMI? ===");
        compareSmokerBMI(records);

        // Problem 15: Sort regions by average charges (descending)
        System.out.println("\n=== Problem 15: Regions by Average Charges (Desc) ===");
        sortRegionsByAverageCharges(records);

        // Problem 17: Southerners smoke more than northerners (rates + avg age)
        System.out.println("\n=== Problem 17: South vs North Smoking Rates & Avg Age ===");
        compareSmokingByRegion(records);

        // Problem 19: Southerners average more children than northerners (report + avg age)
        System.out.println("\n=== Problem 19: South vs North Children & Avg Age ===");
        compareChildrenByRegion(records);

        // Problem 21: Regression charges vs children (+ r + sample predictions)
        System.out.println("\n=== Problem 21: Charges vs Children (Regression + r) ===");
        regressionChargesVsChildren(records);
    }

    // ---- CSV loader using InsuranceRecord (must exist in same folder, no package) ----
    private static List<InsuranceRecord> loadAll(String filename) {
        List<InsuranceRecord> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String header = br.readLine(); if (header == null) return out;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                try { out.add(new InsuranceRecord(parts)); } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Error reading '" + filename + "': " + e.getMessage());
        }
        return out;
    }

    // ---- Problem 3 ----
    private static void printAgeHistogramHorizontal(List<InsuranceRecord> recs, int bins) {
        List<Integer> ages = new ArrayList<>();
        for (InsuranceRecord r : recs) ages.add(r.age);
        if (ages.isEmpty()) { System.out.println("No ages."); return; }
        int min = Collections.min(ages), max = Collections.max(ages);
        int range = max - min;
        int binWidth = (range == 0) ? 1 : (int)Math.ceil((range + 1) / (double)bins);
        int[] counts = new int[bins];
        for (int a : ages) {
            int idx = (a - min) / binWidth;
            if (idx >= bins) idx = bins - 1; if (idx < 0) idx = 0;
            counts[idx]++;
        }
        for (int i = 0; i < bins; i++) {
            int start = min + i * binWidth, end = start + binWidth - 1;
            System.out.printf("%2d–%2d | ", start, end);
            for (int j = 0; j < counts[i]; j++) System.out.print("*");
            System.out.println(" (" + counts[i] + ")");
        }
    }

    // ---- Problem 5 ----
    private static void countChildrenRecords(List<InsuranceRecord> recs) {
        Map<Integer,Integer> map = new TreeMap<>();
        for (InsuranceRecord r : recs) map.put(r.children, map.getOrDefault(r.children, 0) + 1);
        System.out.println("Children -> Count");
        for (Map.Entry<Integer,Integer> e : map.entrySet()) {
            System.out.printf("%8d -> %d%n", e.getKey(), e.getValue());
        }
    }

    // ---- Problem 7 ----
    private static void checkRegionFairness(List<InsuranceRecord> recs) {
        Map<String,Integer> counts = new HashMap<>();
        for (InsuranceRecord r : recs) counts.put(r.region, counts.getOrDefault(r.region, 0) + 1);
        int total = recs.size();
        if (total == 0 || counts.isEmpty()) { System.out.println("No region data."); return; }
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int c : counts.values()) { if (c < min) min = c; if (c > max) max = c; }
        double spread = (max - min) / (double) total; // fraction
        boolean fair = spread <= 0.05;
        System.out.println("Region counts: " + counts);
        System.out.printf("Spread = %.2f%% -> Fair? %b%n", spread * 100.0, fair);
    }

    // ---- Problem 9 ----
    private static void compareChargeRangesByBMI(List<InsuranceRecord> recs) {
        double minA=+1e300, maxA=-1e300, minB=+1e300, maxB=-1e300, minC=+1e300, maxC=-1e300;
        boolean hasA=false, hasB=false, hasC=false;
        for (InsuranceRecord r : recs) {
            if (r.bmi < 30.0) { hasA=true; if (r.charges<minA) minA=r.charges; if (r.charges>maxA) maxA=r.charges; }
            else if (r.bmi <= 45.0) { hasB=true; if (r.charges<minB) minB=r.charges; if (r.charges>maxB) maxB=r.charges; }
            else { hasC=true; if (r.charges<minC) minC=r.charges; if (r.charges>maxC) maxC=r.charges; }
        }
        double rangeA = hasA ? (maxA - minA) : 0.0;
        double rangeB = hasB ? (maxB - minB) : 0.0;
        double rangeC = hasC ? (maxC - minC) : 0.0;
        System.out.printf("Range (BMI<30): %.2f%n", rangeA);
        System.out.printf("Range (30<=BMI<=45): %.2f%n", rangeB);
        System.out.printf("Range (BMI>45): %.2f%n", rangeC);
        System.out.println("Is 30–45 greater than the other two? " + (rangeB > rangeA && rangeB > rangeC));
    }

    // ---- Problem 11 ----
    private static void smokersVsNonSmokersHistogram(List<InsuranceRecord> recs) {
        int smokers = 0, nonsmokers = 0;
        for (InsuranceRecord r : recs) { if (r.smoker) smokers++; else nonsmokers++; }
        int max = Math.max(smokers, nonsmokers);
        System.out.println("Smokers vs Non-Smokers (vertical counts):");
        for (int level = max; level > 0; level--) {
            System.out.printf("%-12s %-12s%n",
                (smokers >= level ? "*" : " "),
                (nonsmokers >= level ? "*" : " "));
        }
        System.out.println("Smokers     NonSmokers");
    }

    // ---- Problem 13 ----
    private static void compareSmokerBMI(List<InsuranceRecord> recs) {
        double sumS=0, sumN=0; int cS=0, cN=0;
        for (InsuranceRecord r : recs) {
            if (r.smoker) { sumS += r.bmi; cS++; } else { sumN += r.bmi; cN++; }
        }
        double avgS = cS>0 ? sumS/cS : 0.0, avgN = cN>0 ? sumN/cN : 0.0;
        System.out.printf("Avg BMI (smokers): %.2f%n", avgS);
        System.out.printf("Avg BMI (non-smokers): %.2f%n", avgN);
        System.out.println("Do smokers average lower BMI? " + (avgS < avgN));
    }

    // ---- Problem 15 ----
    private static void sortRegionsByAverageCharges(List<InsuranceRecord> recs) {
        Map<String,double[]> agg = new HashMap<>();
        for (InsuranceRecord r : recs) {
            double[] a = agg.getOrDefault(r.region, new double[]{0.0,0.0});
            a[0] += r.charges; a[1] += 1.0; agg.put(r.region, a);
        }
        List<Map.Entry<String,double[]>> list = new ArrayList<>(agg.entrySet());
        Collections.sort(list, (e1,e2) -> {
            double avg1 = e1.getValue()[0]/e1.getValue()[1];
            double avg2 = e2.getValue()[0]/e2.getValue()[1];
            return Double.compare(avg2, avg1);
        });
        System.out.println("Region -> Avg Charges (desc)");
        for (Map.Entry<String,double[]> e : list) {
            double avg = e.getValue()[0]/e.getValue()[1];
            System.out.printf("%-10s -> %.2f%n", e.getKey(), avg);
        }
    }

    // ---- Problem 17 ----
    private static void compareSmokingByRegion(List<InsuranceRecord> recs) {
        int southTotal=0, southSmokers=0, southAgeSum=0;
        int northTotal=0, northSmokers=0, northAgeSum=0;
        for (InsuranceRecord r : recs) {
            boolean south = r.region.toLowerCase().contains("south");
            if (south) { southTotal++; if (r.smoker) southSmokers++; southAgeSum += r.age; }
            else { northTotal++; if (r.smoker) northSmokers++; northAgeSum += r.age; }
        }
        double southRate = southTotal>0 ? (100.0*southSmokers/southTotal) : 0.0;
        double northRate = northTotal>0 ? (100.0*northSmokers/northTotal) : 0.0;
        double southAvgAge = southTotal>0 ? (southAgeSum/(double)southTotal) : 0.0;
        double northAvgAge = northTotal>0 ? (northAgeSum/(double)northTotal) : 0.0;
        System.out.printf("South smoking rate: %.2f%% (avg age %.1f)%n", southRate, southAvgAge);
        System.out.printf("North smoking rate: %.2f%% (avg age %.1f)%n", northRate, northAvgAge);
        System.out.println("Do southerners smoke more than northerners? " + (southRate > northRate));
    }

    // ---- Problem 19 ----
    private static void compareChildrenByRegion(List<InsuranceRecord> recs) {
        int southTotal=0, northTotal=0, southKids=0, northKids=0, southAgeSum=0, northAgeSum=0;
        for (InsuranceRecord r : recs) {
            boolean south = r.region.toLowerCase().contains("south");
            if (south) { southTotal++; southKids += r.children; southAgeSum += r.age; }
            else { northTotal++; northKids += r.children; northAgeSum += r.age; }
        }
        double southAvgKids = southTotal>0 ? (southKids/(double)southTotal) : 0.0;
        double northAvgKids = northTotal>0 ? (northKids/(double)northTotal) : 0.0;
        double southAvgAge  = southTotal>0 ? (southAgeSum/(double)southTotal) : 0.0;
        double northAvgAge  = northTotal>0 ? (northAgeSum/(double)northTotal) : 0.0;
        System.out.printf("South avg children: %.2f (avg age %.1f)%n", southAvgKids, southAvgAge);
        System.out.printf("North avg children: %.2f (avg age %.1f)%n", northAvgKids, northAvgAge);
        System.out.println("Do southerners average more children than northerners? " + (southAvgKids > northAvgKids));
    }

    // ---- Problem 21 ----
    private static void regressionChargesVsChildren(List<InsuranceRecord> recs) {
        List<Integer> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();
        for (InsuranceRecord r : recs) { xs.add(r.children); ys.add(r.charges); }
        if (xs.isEmpty()) { System.out.println("No data."); return; }

        double meanX=0, meanY=0;
        for (int x : xs) meanX += x; meanX /= xs.size();
        for (double y : ys) meanY += y; meanY /= ys.size();

        double num = 0.0, den = 0.0;
        for (int i = 0; i < xs.size(); i++) {
            num += (xs.get(i) - meanX) * (ys.get(i) - meanY);
            den += Math.pow(xs.get(i) - meanX, 2);
        }
        double slope = (den == 0.0) ? 0.0 : (num / den);
        double intercept = meanY - slope * meanX;
        System.out.printf("Regression (charges = %.2f + %.2f * children)%n", intercept, slope);

        double sumXY=0, sumX2=0, sumY2=0;
        for (int i = 0; i < xs.size(); i++) {
            double dx = xs.get(i) - meanX, dy = ys.get(i) - meanY;
            sumXY += dx*dy; sumX2 += dx*dx; sumY2 += dy*dy;
        }
        double r = (sumX2 == 0 || sumY2 == 0) ? 0.0 : (sumXY / Math.sqrt(sumX2 * sumY2));
        System.out.printf("Pearson r: %.4f%n", r);

        System.out.println("Predicted charges (children -> y):");
        for (int x = 0; x <= 10; x++) {
            double yhat = intercept + slope * x;
            System.out.printf("%2d -> %.2f%n", x, yhat);
        }
    }
}
