package rainfallPackage.beta.analyser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class run the analyser.
 * analysed files are kept on a txt file, which is used to populate the visualiser
 */

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    public static List<RainfallAnalyser> dataFiles = new ArrayList<>();

    private static final String FILE_COLLECTION_NAME = "fileCollection.txt";


    public static void main(String[] args) {
        populateDataFiles();

        welcomeText();
        printSelection();
        int choice = userInput();

        while(choice != 0) {
            switch (choice) {
                case 1 -> printInstructions();
                case 2 ->  printCurrentAnalysedFiles();
                case 3 -> {
                    System.out.println("Lets analyse a file :)\nfile name or path:");
                    String fileName = scanner.nextLine();
                    System.out.println("Name for Object:");
                    String name = scanner.nextLine();

                    if(checkNewData(fileName)) {
                        createRainfallObject(name, fileName);
                    }
                    else System.out.println("Sorry this file already exists");
                }
            }
            choice = userInput();
        }
        writeFile();
        System.out.println("All done. Now go over to RainfallVisualiser and see the new files there. BYE");
    }

    private static void welcomeText() {
        System.out.println("""
                Welcome to the Rainfall Calculator Tool!!!
                Here you will be able to use your downloaded CSV rainfall data files, that will be analysed and
                reformatted. It will then be able to be used in our visual tool.
                These files can be found on BOM climate website.
                """);
    }

    private static int userInput() {
        int choice = 0;
        System.out.println("Please choose an option:");
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Please ensure an Integer value is inputted");
        }
        scanner.nextLine();
        return choice;
    }

    private static boolean checkNewData(String fileName) {
        for(RainfallAnalyser rainfallAnalyser : dataFiles) {
            if(rainfallAnalyser.getRawFileName().equals(fileName)) return false;
        }
        return true;
    }

    private static void createRainfallObject(String name, String fileName) {
        RainfallAnalyser rainfallAnalyser = new RainfallAnalyser(name);
        rainfallAnalyser.setFileName(fileName);
        rainfallAnalyser.readFile();
        rainfallAnalyser.analyseData();
        try {
            rainfallAnalyser.writeFile();
            dataFiles.add(rainfallAnalyser);
        }catch (StringIndexOutOfBoundsException e) {
            System.out.println("ERROR: Sorry, cannot create save path or write new file as there was " +
                    "a problem reading from the file.");
            System.out.println("Exception Thrown:" + e);
        }
    }

    private static void printSelection() {
        System.out.println("""
                0.  Exit
                1.  How the program works
                2.  See current rainfall files that have been analysed
                3.  Analyse new rainfall file
                """);
    }

    private static void printInstructions() {
        System.out.println("""
                Hi, so you need to used a downloaded csv file of rainfall data.
                Head over to bom.gov.au/climate/data
                Find a location you want click "Get Data"
                Then in the redirected window, clicked "All Years of Data"
                """);
    }

    private static void populateDataFiles() {
        try (BufferedReader locFile = new BufferedReader(new FileReader(FILE_COLLECTION_NAME))) {
            String input;
            while ((input = locFile.readLine()) != null) {
                String[] line = input.split(",");

                // In try and catch block incase fileCollections.txt is empty (No files analysed yet).
                try {
                    createRainfallObject(line[0], line[1]);
                }catch (ArrayIndexOutOfBoundsException ignored) {}

            }
        } catch (IOException e) {
            System.out.println("ERROR: There is a problem with the file name or contents.");
        }
    }

    private static void writeFile() {
        try (BufferedWriter locFile = new BufferedWriter(new FileWriter(FILE_COLLECTION_NAME))) {
            for (RainfallAnalyser data : dataFiles) {
                locFile.write(String.format("%s,%s,%s\n", data.getName(), data.getRawFileName(), data.getNewFileName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printCurrentAnalysedFiles() {
        if(dataFiles.size() == 0)
            System.out.println("Sorry: There are currently no analysed files! Why don't you try add one and check again");
        for(RainfallAnalyser rainfallAnalyser : dataFiles) {
            System.out.println(rainfallAnalyser.getName() + "," + rainfallAnalyser.getRawFileName() + "," + rainfallAnalyser.getNewFileName());
        }
    }
}
