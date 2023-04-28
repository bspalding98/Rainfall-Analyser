package rainfallPackage.alpha;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The program reads a CSV file that contains daily rainfall data over several years
 * It is then analysed and a new CSV file is written containing the following:
 * Year, month, each month's total rainfall, each month's min rainfall, and each month's max rainfall
 */
public class RainfallAnalyserAlpha {

    private static final List<String[]> dataset = new ArrayList<>();    // Stores unprocessed CSV file
    private static final List<String> analysedDataset = new ArrayList<>();

    public static final String ANALYSED_FILE_NAME = "MountSheridanStationCNSAnalysed.csv";
    private static final String FILE_NAME = "MountSheridanStationCNS.csv";

    private static int previousDay = 0;
    private static double maxRainfallForMonth = 0;
    private static double minRainfallForMonth = Double.MAX_VALUE;
    private static double totalRainfall = 0;


    public static void main(String[] args) {
        readFile();
        if (dataset.get(0) == null) throw new IllegalArgumentException("ERROR: File is empty");
        analysedDataset();
        writeFile();
    }

    /**
     * Reads the raw CSV file of rainfall data into an arraylist to be used to analyse and create the new CSV file
     */
    private static void readFile() {
        try (BufferedReader locFile = new BufferedReader(new FileReader(RainfallAnalyserAlpha.FILE_NAME))) {
            String input;
            while ((input = locFile.readLine()) != null) {
                String[] line = input.split(",");
                dataset.add(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR: There is a problem with the file name or contents.");
        }
    }

    /**
     * Method analyses the data and appends each month's rainfall statistics to a new arraylist
     */
    private static void analysedDataset() {
        int previousMonth = 0;
        int previousYear = 0;

        // Adding in Header
        String[] title = dataset.get(0);
        analysedDataset.add(String.format("%s,%s,%s,%s,%s",
                title[2], title[3], "Minimum Monthly Rainfall(mm)", "Maximum Monthly Rainfall(mm)", "Total Rainfall(mm)"));
        dataset.remove(0);

        for (String[] line : dataset) {
            int year = Integer.parseInt(line[2]);
            int month = Integer.parseInt(line[3]);
            int day = Integer.parseInt(line[4]);
            double rainfall = (line.length < 7 ? 0.00 : Double.parseDouble(line[5]));

            if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                throw new IllegalArgumentException("ERROR: Month is out of range.");
            }
            if (day < previousDay) {
                addToNewDataset(previousYear, previousMonth);
                resetStoredData();
            }
            previousMonth = month;
            previousYear = year;
            previousDay++;
            calculateRainfallStats(rainfall);
        }
        addToNewDataset(previousYear, previousMonth);
    }

    /**
     * Write the new analysed CSV using the new arraylist created from the analyseDataset()
     */
    private static void writeFile() {
        try (BufferedWriter locFile = new BufferedWriter(new FileWriter(ANALYSED_FILE_NAME))) {
            for (String line : analysedDataset) {
                locFile.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a string with correct format for analysed CSV file and is saved to analysed arraylist
     */
    private static void addToNewDataset(int year, int previousMonth) {
        String newLine = String.format("%d,%d,%.2f,%.2f,%.2f", year, previousMonth, minRainfallForMonth, maxRainfallForMonth, totalRainfall);
        analysedDataset.add(newLine);
    }

    /**
     * Calculates the rainfall for each month.
     */
    private static void calculateRainfallStats(double rainfall) {
        totalRainfall += rainfall;
        if (rainfall < minRainfallForMonth) minRainfallForMonth = rainfall;
        if (rainfall > maxRainfallForMonth) maxRainfallForMonth = rainfall;
    }

    /**
     * Resets the data after each month so no values leak over into other months calculations
     */
    private static void resetStoredData() {
        previousDay = 0;
        minRainfallForMonth = Double.MAX_VALUE;
        maxRainfallForMonth = 0;
        totalRainfall = 0;
    }
}