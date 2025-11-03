import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void processMarijuana(Sample sample) throws TestFailedException {
        // Implementation for processing marijuana samples
        sample.getTests().add("Gas Chromatography");
        // bounds is 5:47 and 6:14 seconds
        int min = 347; // 5:47 in seconds
        int max = 374; // 6:14 in seconds
        if (sample.getGcTime() < min || sample.getGcTime() > max) {
            throw new TestFailedException("Separation time out of bounds", sample);
        }
        sample.getTests().add("Mass Spectrometry");
        // Check for expected mass spectrometry peaks
        String[] peaks = sample.getMsPeaks().split(",");
        int matches = 0;
        int[] expectedPeaks = {314, 299, 231};
        for (String p : peaks) {
            for (int m : expectedPeaks) {
                if (p.equals(String.valueOf(m))) {
                    matches++;
                }
            }
        }
        if (matches < 2) {
            throw new TestFailedException("Insufficient peak matches", sample);
        }

        //Gas Chromatography Abundance Test
        sample.getTests().add("Gas Chromatography Abundance");
        double abundance = Double.parseDouble(sample.getMiscTest());
        if (abundance < 0.3) {
            throw new TestFailedException("Concentration below 0.3%", sample);
        }
    }
    //cocaine tests
    public static void processCocaine(Sample sample) throws TestFailedException {
        //Implementation for processing cocaine samples
        sample.getTests().add("Gas Chromatography");
        //bounds is 6:38 to 7:02 seconds
        int min = 398; //6:38 in seconds
        int max = 422; //7:02 in seconds
        if (sample.getGcTime() < min || sample.getGcTime() > max) {
            throw new TestFailedException("Separation time out of bounds", sample);
        }
        //Mass Spectrometry Test
        sample.getTests().add("Mass Spectrometry");
        String[] peaks = sample.getMsPeaks().split(",");
        int matches = 0;
        int[] expectedPeaks = {149, 91, 58};
        for (String p : peaks) {
            for (int m : expectedPeaks) {
                if (p.equals(String.valueOf(m))) {
                    matches++;
                }
            }
        }
        if (matches < 2) {
            throw new TestFailedException("Insufficient peak matches", sample);
        }
        //UV Spectroscopy Test
        sample.getTests().add("UV Spectroscopy");
        int uv = Integer.parseInt(sample.getMiscTest());
        if (uv < 192 || uv > 202) {
            throw new TestFailedException("UV reading out of range", sample);
        }
    }

    // Methamphetamine tests
    public static void processMethamphetamine(Sample sample) throws TestFailedException {
        // Implementation for processing methamphetamine samples
        sample.getTests().add("Gas Chromatography");
        //bounds is 5:07 to 5:16 seconds
        int min = 307; //5:07 in seconds
        int max = 316; //5:16 in seconds
        if (sample.getGcTime() < min || sample.getGcTime() > max) {
            throw new TestFailedException("Separation time out of bounds", sample);
        }
        //Mass Spectrometry Test
        sample.getTests().add("Mass Spectrometry");
        String[] peaks = sample.getMsPeaks().split(",");
        int matches = 0;
        int[] expectedPeaks = {303, 182, 82};
        for (String p : peaks) {
            for (int m : expectedPeaks) {
                if (p.equals(String.valueOf(m))) {
                    matches++;
                }
            }
        }
        if (matches < 2) {
            throw new TestFailedException("Insufficient peak matches", sample);
        }

        //Logo ID Test
        sample.getTests().add("Logo ID");
        String parts[] = sample.getMiscTest().split(" ");
        if (parts.length == 3) {
            String engraving = parts[0];
            String shape = parts[1];
            String color = parts[2];
            if ((engraving.equals("R-12") && shape.equals("round") && color.equals("white")) ||
                (engraving.equals("V-20") && shape.equals("oval") && color.equals("blue")) ||
                    (engraving.equals("A-65") && shape.equals("capsule") && color.equals("pink"))) {
                throw new TestFailedException("Prescription medication", sample);
            }
        }
    }

    public static void processFile(Scanner scan) throws IOException {
        PrintWriter passed = new PrintWriter(new FileWriter("passed.txt"));
        PrintWriter failed = new PrintWriter(new FileWriter("failed.txt"));

        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            String drugType = parts[0].trim().toLowerCase();
            double weight = Double.parseDouble(parts[1]);
            int gcTime = Integer.parseInt(parts[2]);
            String ms = parts[3].trim();
            String misc = parts[4].trim();

            Sample sample = new Sample(drugType, weight, gcTime, ms, misc);
            try {
                if (drugType.equals("marijuana")) {
                    processMarijuana(sample);
                } else if (drugType.equals("cocaine")) {
                    processCocaine(sample);
                } else if (drugType.equals("methamphetamine")) {
                    processMethamphetamine(sample);
                } else {
                    throw new TestFailedException("Unknown drug type", sample);
                }

                String penalty;
                if (drugType.equals("cocaine") || drugType.equals("methamphetamine")) {
                    if (weight < 1) {
                        penalty = "Up to 3 years in prison";
                    }
                    else if (weight < 4) {
                        penalty = "1 - 8 years in prison";
                    }
                    else if (weight < 28) {
                        penalty = "1 - 15 years in prison";
                    }
                    else if (weight < 200) {
                        penalty = "Minimum 10 years, $200,000 fine";
                    }
                    else if(weight < 400) {
                        penalty = "Minimum 15 years, $300,000 fine";
                    }
                    else {
                        penalty = "Minimum 25 years, $1,000,000 fine";
                    }
                } else {
                    if (weight < 28.35) {
                        penalty = "Misdemeanor";
                    }
                    else if (weight < 4535) {
                        penalty = "1 - 10 years in prison";
                    }
                    else if (weight < 907184.7) {
                        penalty = "Minimum 5 years, $100,000 fine";
                    }
                    else if(weight < 4535924) {
                        penalty = "Minimum 7 years, $250,000 fine";
                    }
                    else {
                        penalty = "Minimum 15 years, $1,000,000 fine";
                    }
                }

                passed.printf("%s\nResult: Positive, %s\n====\n", sample.toString(), penalty);
            }

            catch (TestFailedException e) {
                failed.printf("%s\nResult: Negative, %s\n====\n", sample.toString(), e.getMessage());
            }
        }
        passed.flush();
        failed.flush();
        passed.close();
        failed.close();
    }

    public static void main(String[] args) {
        System.out.println("[Drug Report Analyzer]");
        System.out.print("Enter name of drug file: ");

        Scanner input = new Scanner(System.in);
        String filename = input.nextLine();
        File file = new File(filename);

        if(!file.exists()) {
            System.out.println("Could not find file '" + filename + "'");
            System.out.println("Program complete");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            System.out.println("File loaded, processing...");
            processFile(fileScanner);
            System.out.println("File processed. Outputs written to 'passed.txt' and 'failed.txt'.");
        }
        catch (IOException e) {
            System.out.println("Error writing output files.");
        }
        System.out.println("Program complete.");
    }
}