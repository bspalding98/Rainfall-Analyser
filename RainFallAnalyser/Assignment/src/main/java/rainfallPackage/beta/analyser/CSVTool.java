package rainfallPackage.beta.analyser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to read from a raw file and write to an analysed file.
 */

public class CSVTool {

    private final List<Data> dataset = new ArrayList<>();

    public void readFile(String fileName) {
        try (BufferedReader locFile = new BufferedReader(new FileReader(fileName))) {
            String input;
            locFile.readLine();
            while ((input = locFile.readLine()) != null) {
                String[] line = input.split(",");
                Data data = new Data(line);
                dataset.add(data);
            }
        } catch (IOException e) {
            System.out.println("ERROR: There is a problem with reading from the file");
        }
    }


    public void writeFile(String newFileName, List<String> analysedData) {
        try (BufferedWriter locFile = new BufferedWriter(new FileWriter(newFileName))) {
            for (String line : analysedData) {
                locFile.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR: There is a problem with writing to the file");
            e.printStackTrace();
        }
    }

    public List<Data> getDataset() {
        return dataset;
    }
}
