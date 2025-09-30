import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetInfo {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "insurance.csv";

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

            System.out.printf("Indices -> age: %d, bmi: %d, children: %d, charges: %d%n",
                              ageIndex, bmiIndex, childrenIndex, chargesIndex);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}