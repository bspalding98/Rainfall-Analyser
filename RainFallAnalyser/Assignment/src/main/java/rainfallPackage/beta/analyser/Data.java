package rainfallPackage.beta.analyser;

/**
 * This is class holds a line of Data in the raw data files.
 */

public class Data {

    private int year;
    private int month;
    private int day;
    private double rainfall;

    private final String[] values;


    public Data(String[] values) {
        this.values = values;
        createVariables(values);
    }

    private void createVariables(String[] values) {
        this.year = Integer.parseInt(values[2]);
        this.month = Integer.parseInt(values[3]);
        this.day = Integer.parseInt(values[4]);
        this.rainfall = (values.length < 7 ? 0.00 : Double.parseDouble(values[5]));
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public double getRainfall() {
        return rainfall;
    }

    public String[] getValues() {
        return values;
    }
}
