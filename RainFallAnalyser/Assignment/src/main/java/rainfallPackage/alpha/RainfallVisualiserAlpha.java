package rainfallPackage.alpha;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX Application reads a CSV file containing rainfall data from a location and displays its monthly
 * rainfall total in a form of a BarChart
 */

public class RainfallVisualiserAlpha extends Application{
    // Editable constants & variables
    private final int SCENE_WIDTH = 800;
    private final int SCENE_HEIGHT = 600;
    private final int BAR_CHART_BAR_GAP = 1;
    private final int BAR_CHART_CATEGORY_GAP = 5;

    // Global variables & constants
    private XYChart.Series<String, Number>[] monthlyBarChartDataset;

    private final List<String[]> dataset = new ArrayList<>();

    private final int WINDOWS_WIDTH = SCENE_WIDTH + 50;
    private final int WINDOWS_HEIGHT = SCENE_HEIGHT + 100;



    public void start(Stage primaryStage) {
        // Creating BarChart and XY Axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Year");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rainfall (In Millimeters)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setBarGap(BAR_CHART_BAR_GAP);
        barChart.setCategoryGap(BAR_CHART_CATEGORY_GAP);

        readFile();
        initialiseMonthlyBarChartDataset();
        drawBarChart();

        barChart.getData().addAll(monthlyBarChartDataset);  // Add Series to BarChart.


        barChart.setTitle("MountSheridanStation");
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(barChart); // Center so it fills the whole size of the borderPane

        primaryStage.setTitle("Monthly Rainfall Calculator");
        Scene scene = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.setWidth(WINDOWS_WIDTH);
        primaryStage.setHeight(WINDOWS_HEIGHT);

        primaryStage.show();
    }

    /**
     * Reads the analysed CSV file of rainfall data into an arraylist to be used for the GUI:
     */
    private void readFile() {
        try (BufferedReader locFile = new BufferedReader(new FileReader(RainfallAnalyserAlpha.ANALYSED_FILE_NAME))) {
            String input;
            locFile.readLine(); // Used to skip the header
            while((input = locFile.readLine()) != null) {
                String[] line = input.split(",");
                dataset.add(line);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Initialises the BarChart Series (X Categories):
     */
    private void initialiseMonthlyBarChartDataset() {
        monthlyBarChartDataset = new XYChart.Series[12];    // Hardcoded as it's assumed 12 months to a year
        for(int i = 0; i < 12; i++) {
            monthlyBarChartDataset[i] = new XYChart.Series<>();
        }
    }

    /**
     * Draws the Bar Chart Graph:
     * Done by reading through the file stored in the arraylist and appending appropriate data
     * to the corresponding months.
     */
    private void drawBarChart() {
        for (String[] line : dataset) {
            String year = line[0];
            int month = Integer.parseInt(line[1]);
            double monthlyRainFail = Double.parseDouble(line[4]);

            monthlyBarChartDataset[month - 1].getData().add(new XYChart.Data<>(year, monthlyRainFail));
            monthlyBarChartDataset[month - 1].setName("Month " + month);
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
