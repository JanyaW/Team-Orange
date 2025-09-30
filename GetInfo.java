import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetInfo {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Double> ages = new ArrayList<>();
        List<Double> bmis = new ArrayList<>();
        List<Double> children = new ArrayList<>();
        List<Double> charges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

            String[] headers = headerLine.split(",");
            int ageIndex = -1, bmiIndex = -1, childrenIndex = -1, chargesIndex = -1;
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

        System.out.println("Ages: " + ages.size());
        System.out.println("BMIs: " + bmis.size());
        System.out.println("Children: " + children.size());
        System.out.println("Charges: " + charges.size());
    }
}