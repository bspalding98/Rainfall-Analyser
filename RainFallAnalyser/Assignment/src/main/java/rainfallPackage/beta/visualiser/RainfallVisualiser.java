package rainfallPackage.beta.visualiser;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Visualiser which displays all analysed csv rainfall files from the txt file created by the Analyser
 */

public class RainfallVisualiser extends Application {

    // Editable constants & variables
    private final int SCENE_WIDTH = 1000;
    private final int SCENE_HEIGHT = 800;
    private final int BAR_CHART_BAR_GAP = 1;
    private final int BAR_CHART_CATEGORY_GAP = 5;

    // Global variables & constants
    private XYChart.Series<String, Number>[] monthlyBarChartDataset;
    private List<String[]> dataset = new ArrayList<>();

    private BorderPane borderPane;
    private Timeline tl;
    private BarChart<String, Number> barChart;

    private final int WINDOWS_WIDTH = SCENE_WIDTH + 50;
    private final int WINDOWS_HEIGHT = SCENE_HEIGHT + 100;


    public void start(Stage primaryStage) throws IOException {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Year");
        xAxis.setAnimated(false);   // Needed to fix layout bug happening

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rainfall (In Millimeters)");

        // Create BarChart: setting bar and category gap
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setBarGap(BAR_CHART_BAR_GAP);
        barChart.setCategoryGap(BAR_CHART_CATEGORY_GAP);

        // Creating dropBox, populating it with Analysed files and creating functionality:
        ComboBox<String> comboBox = new ComboBox<>();
        createComboBoxFunctionality(comboBox);
        comboBox.getSelectionModel().selectFirst();

        // Adding to borderPane
        borderPane = new BorderPane();
        borderPane.setTop(comboBox);
        BorderPane.setAlignment(comboBox, Pos.CENTER);
        borderPane.setCenter(barChart); // Center so it fills the rest of the screen


        primaryStage.setTitle("Monthly Rainfall Calculator");
        Scene scene = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.setWidth(WINDOWS_WIDTH);
        primaryStage.setHeight(WINDOWS_HEIGHT);

        primaryStage.show();
    }


    /**
     * Reads CSV file that was passed in, which will only be analysed files by the way it's coded
     * Boolean skip was added so the method can be reused for other files that don't contain a header.
     */
    private void readFile(String fileName, boolean skip) {
        try (BufferedReader locFile = new BufferedReader(new FileReader(fileName))) {
            String input;
            if (skip) locFile.readLine(); // Used to skip the header
            while ((input = locFile.readLine()) != null) {
                String[] line = input.split(",");
                dataset.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Functionality for Combo Box:
     * Uses a text file where all analysed fileNames are stored in order to create the right length and name for
     * each choice.
     * Choices only consist of analysed data (All choices should work correctly then) and a Home page.
     * Has a listener which listens to the selection and reads the appropriate file and creates the graphs.
     * This allows for the graph to be dynamic and be able to switch between graphs.
     */
    private void createComboBoxFunctionality(ComboBox<String> comboBox) {
        readFile("C:\\Users\\boyd9\\Documents\\AssignmentProject\\Assignment\\fileCollection.txt", false);
        List<String[]> files = new ArrayList<>(dataset);
        comboBox.getItems().add("Home");
        for (String[] line : files) {
            comboBox.getItems().add(line[0]);
        }

        comboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldString, newString) -> {
            // These clear the barchart and all stored data to reload the new one
            barChart.getData().clear();
            dataset.clear();

            int index = 0;
            for (String[] line : files) {
                if (newString.equals(line[0]))
                    index = files.indexOf(line);
            }

            if (!(newString.equals("Home"))) {
                tl.stop();
                readFile("C:\\Users\\boyd9\\Documents\\AssignmentProject\\Assignment\\" +files.get(index)[2], true);
                initialiseMonthlyBarChartDataset();
                drawBarChart();

                barChart.getData().addAll(monthlyBarChartDataset);
                addEventHandlers();
            } else {
                homePage();
            }
        });
    }

    /**
     * Adding simple Event Handlers to each bar of Data in the graph.
     */
    private void addEventHandlers() {
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                addStatsWindow(data, series);
                addHoverAnimation(data);
            }
        }
    }

    /**
     * Creates A homepage:
     * This homepage just consists of an animated barchart to showcase what the program is about.
     */
    private void homePage() {
        String itemA = "A";
        String itemB = "B";
        String itemC = "C";

        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.setName("Welcome");
        series1.getData().add(new XYChart.Data(itemA, 2));
        series1.getData().add(new XYChart.Data(itemB, 20));
        series1.getData().add(new XYChart.Data(itemC, 10));

        XYChart.Series<String, Number> series2 = new XYChart.Series();
        series2.setName("To");
        series2.getData().add(new XYChart.Data(itemA, 50));
        series2.getData().add(new XYChart.Data(itemB, 41));
        series2.getData().add(new XYChart.Data(itemC, 45));

        XYChart.Series<String, Number> series3 = new XYChart.Series();
        series3.setName("HomePage");
        series3.getData().add(new XYChart.Data(itemA, 45));
        series3.getData().add(new XYChart.Data(itemB, 44));
        series3.getData().add(new XYChart.Data(itemC, 18));

        tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(1000), actionEvent -> {
            for (XYChart.Series<String, Number> series : barChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    data.setYValue(Math.random() * 100);
                }
            }
        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

        barChart.getData().addAll(series1, series2, series3);
    }

    /**
     * Creates a new popout window on top of current window with additional rainfall information
     * Made Modal with .showAndWait() so cannot open several windows and must close the window to interact with main window again.
     * Done to stop from several windows being opened in the background
     */
    private void addStatsWindow(XYChart.Data<String, Number> data, XYChart.Series<String, Number> series) {
        data.getNode().setOnMousePressed((MouseEvent event) -> {

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(borderPane.getScene().getWindow());
            dialog.setTitle("Monthly Statistics");

            dialog.getDialogPane().setContentText(
                    "Year:\t " + data.getXValue() + "\n" + series.getName() + "\n" +
                            "Total Rainfall:\t" + data.getYValue() + "\n" +
                            "Minimum Rainfall:\t" + dataset.get((Integer) data.getExtraValue())[2] + "\n" +
                            "Maximum Rainfall:\t" + dataset.get((Integer) data.getExtraValue())[3]
            );
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);  // Needed as X would not close the program (bug)
            dialog.showAndWait();
        });
    }

    /**
     * Created as there are a lot of bars and some are small and hard to click, so having an
     * event where the bars color changes to black if the mouse is currently hovered on it, makes it easier to
     * click on which bar the user wants.
     */
    private void addHoverAnimation(XYChart.Data<String, Number> data) {
        data.getNode().setOnMouseEntered((MouseEvent event) -> data.getNode().setStyle("-fx-bar-fill: black;"));

        data.getNode().setOnMouseExited((MouseEvent event) -> data.getNode().setStyle("-fx-base: initial"));
    }

    /**
     * Initialises each Monthly Bar
     * Hardcoded the size of the Monthly Bars per year as there are only ever 12 months in year.
     */
    private void initialiseMonthlyBarChartDataset() {
        monthlyBarChartDataset = new XYChart.Series[12];
        for (int i = 0; i < 12; i++) {
            monthlyBarChartDataset[i] = new XYChart.Series<>();
        }
    }

    /**
     * Draws the BarChart Bars:
     * Done by adding the Data for the bar to the appropriate XYChart.Series based on the month
     * Is then Added to barchart in start()
     */
    private void drawBarChart() {
        int extraValue = 0;
        for (String[] line : dataset) {
            String year = line[0];
            int month = Integer.parseInt(line[1]);
            double monthlyRainFail = Double.parseDouble(line[4]);

            monthlyBarChartDataset[month - 1].getData().add(new XYChart.Data<>(year, monthlyRainFail, extraValue));
            monthlyBarChartDataset[month - 1].setName("Month " + month);
            extraValue++;
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}


