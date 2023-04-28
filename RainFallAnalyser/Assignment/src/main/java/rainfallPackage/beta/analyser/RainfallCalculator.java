package rainfallPackage.beta.analyser;

import java.util.ArrayList;
import java.util.List;

/**
 * Where the calculations are made and stored for the new Data file.
 */

public class RainfallCalculator {
    private int previousDay = 0;
    private double minRainfallForMonth = Double.MAX_VALUE;
    private double maxRainfallForMonth = 0;
    private double totalRainfall = 0;

    private final List<String> analysedData = new ArrayList<>();


    public void calculateMonthlyRainfall(List<Data> rainfallData) {

        int previousMonth = 0;
        int previousYear = 0;

        // Adding in Header
        analysedData.add(String.format("%s,%s,%s,%s,%s",
                "Year", "Month", "Minimum Monthly Rainfall(mm)", "Maximum Monthly Rainfall(mm)", "Total Rainfall(mm)"));

        for (Data data : rainfallData) {
            int year = data.getYear();
            int month = data.getMonth();
            int day = data.getDay();
            double rainfall = data.getRainfall();

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

    private  void addToNewDataset(int year, int previousMonth) {
        String newLine = String.format("%d,%d,%.2f,%.2f,%.2f", year, previousMonth, minRainfallForMonth, maxRainfallForMonth, totalRainfall);
        analysedData.add(newLine);
    }

    private  void calculateRainfallStats(double rainfall) {
        totalRainfall += rainfall;
        if (rainfall < minRainfallForMonth) minRainfallForMonth = rainfall;
        if (rainfall > maxRainfallForMonth) maxRainfallForMonth = rainfall;
    }

    private  void resetStoredData() {
        previousDay = 0;
        minRainfallForMonth = Double.MAX_VALUE;
        maxRainfallForMonth = 0;
        totalRainfall = 0;
    }


    public List<String> getAnalysedData() {
        return analysedData;
    }
}
