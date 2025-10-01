import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmokerHistogram {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";
        List<String> smokers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) return;

            String[] headers = headerLine.split(",");
            int smokerIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("smoker")) {
                    smokerIndex = i;
                    break;
                }
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                smokers.add(parts[smokerIndex].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("First 10 smoker values:");
        for (int i = 0; i < Math.min(10, smokers.size()); i++) {
            System.out.println(smokers.get(i));
        }
    }
}