import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChargesPerChild {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

        List<Integer> childrenList = new ArrayList<>();
        List<Double> chargesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty: " + filename);
                return;
            }

            String[] headers = headerLine.split(",");
            int childrenIndex = -1, chargesIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                String col = headers[i].trim().toLowerCase();
                if (col.equals("children")) childrenIndex = i;
                else if (col.equals("charges")) chargesIndex = i;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    int children = Integer.parseInt(parts[childrenIndex].trim());
                    double charge = Double.parseDouble(parts[chargesIndex].trim());
                    childrenList.add(children);
                    chargesList.add(charge);
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.printf("Read %d rows of children/charges data.%n", childrenList.size());

        double totalCharges = 0.0;
        int totalChildren = 0;

        for (int i = 0; i < childrenList.size(); i++) {
            totalCharges += chargesList.get(i);
            totalChildren += childrenList.get(i);
        }

        double avgChargePerChild = totalChildren > 0 ? totalCharges / totalChildren : 0.0;

        System.out.printf("Total charges: %.2f%n", totalCharges);
        System.out.printf("Total children: %d%n", totalChildren);
        System.out.printf("Average charge per child: %.2f%n", avgChargePerChild);
    }
}
