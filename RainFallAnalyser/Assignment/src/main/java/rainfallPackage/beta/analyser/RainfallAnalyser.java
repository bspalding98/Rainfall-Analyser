package rainfallPackage.beta.analyser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parent class which uses composition to use its child classes
 */

public class RainfallAnalyser {
    private final String name;
    private String fileName;
    private String newFileName;


    private final CSVTool csvTool = new CSVTool();
    private final RainfallCalculator calculator = new RainfallCalculator();

    private List<Data> nonAnalysedData = new ArrayList<>();
    private List<String> analysedData = new ArrayList<>();

    public RainfallAnalyser(String name) {
        this.name = name;
    }

    public void readFile() {
        csvTool.readFile(fileName);
        nonAnalysedData = csvTool.getDataset();
    }

    public void writeFile() {
        setNewFileName();
        csvTool.writeFile(newFileName, analysedData);
    }

    public void analyseData() {
        calculator.calculateMonthlyRainfall(nonAnalysedData);
        analysedData = calculator.getAnalysedData();
    }


    public void printNonAnalysedData() {
        for(Data data : nonAnalysedData) {
            System.out.println(Arrays.toString(data.getValues()));
        }
    }

    public void printAnalysedData() {
        for(String data : analysedData) {
            System.out.println(data);
        }
    }

    private String createSavePath(String fileName) {
        return fileName.substring(0, fileName.indexOf(".")) + "Analysed.csv";
    }


    //Setters:
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void setNewFileName() {
        newFileName = createSavePath(fileName);
    }

    // Getters:
    public List<Data> getDataset() {
        return nonAnalysedData;
    }

    public String getNewFileName() {return newFileName;}

    public String getName() {return name;}

    public String getRawFileName() {return fileName;}
}
