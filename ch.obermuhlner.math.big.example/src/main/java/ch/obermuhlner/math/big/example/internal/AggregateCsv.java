package ch.obermuhlner.math.big.example.internal;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class AggregateCsv {
    public void run(String outFileName, String filePattern, List<String> releaseNames, List<String> printRowHeaders) {
        System.out.println("Writing " + outFileName);
        try (PrintWriter out = new PrintWriter(new FileWriter(outFileName))) {
            List<String> rowHeaders = new ArrayList<>();
            Map<String, Map<String, String>> fileValueMaps = new HashMap<>();

            for (String releaseName : releaseNames) {
                String file = String.format(filePattern, releaseName);
                try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                    String line = in.readLine();
                    while (line != null) {
                        if (!line.startsWith("#")) {
                            String[] cells = line.trim().split(Pattern.quote(","));

                            String rowHeader = cells[0].trim();
                            if (!rowHeaders.contains(rowHeader)) {
                                rowHeaders.add(rowHeader);
                            }
                            Map<String, String> valueMap = fileValueMaps.computeIfAbsent(releaseName, key -> {
                                return new HashMap<>();
                            });

                            String value = cells[3].trim();
                            valueMap.put(rowHeader, value);
                        }

                        line = in.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            rowHeaders.remove("Name");

            if (printRowHeaders != null) {
                rowHeaders = printRowHeaders;
            }

            out.print("Function");
            for (String rowHeader : rowHeaders) {
                out.print(",");
                out.print(rowHeader);
            }
            out.println();

            for (String releaseName : releaseNames) {
                out.print(releaseName);
                for (String rowHeader : rowHeaders) {
                    String value = fileValueMaps.get(releaseName).get(rowHeader);
                    out.print(",");
                    out.print(value);
                }
                out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AggregateCsv aggregateCsv = new AggregateCsv();

        List<String> releaseNames = Arrays.asList(
                "v1_0_0",
                "v1_1_0",
                "v1_2_0",
                "v1_2_1",
                "v1_3_0",
                "v2_0_0",
                "v2_0_1",
                "v2_1_0",
                "v2_2_0",
                "v2_2_1",
                "v2_3_0",
                "v2_3_1");

        aggregateCsv.run("performance over releases.csv", "../%s/performance.csv", releaseNames, null);

        aggregateCsv.run("selected functions performance over releases.csv", "../%s/performance.csv", releaseNames, Arrays.asList(
                "log(0.1)",
                "exp(2)",
                "sqrt(2)",
                "pow(2;0.1)",
                "root(2;3)",
                "sin(2)",
                "cos(2)",
                "tan(2)",
                "cot(2)",
                "asin(0.1)",
                "acos(0.1)",
                "atan(0.1)",
                "acot(0.1)",
                "sinh(2)",
                "cosh(2)",
                "tanh(2)",
                "asinh(0.1)",
                "acosh(2)",
                "atanh(0.1)",
                "acoth(2)",
                "coth(2)"));

        aggregateCsv.run("fast functions performance over releases.csv", "../%s/performance.csv", releaseNames, Arrays.asList(
                "log(1)",
                "log(10)",
                "log(100)",
                "sqrt(2)",
                "pow(2;3)"));

        aggregateCsv.run("medium functions performance over releases.csv", "../%s/performance.csv", releaseNames, Arrays.asList(
                "root(2;3)",
                "sin(2)",
                "cos(2)",
                "sinh(2)",
                "cosh(2)"));

        aggregateCsv.run("slow functions performance over releases.csv", "../%s/performance.csv", releaseNames, Arrays.asList(
                "log(0.5)",
                "pow(2;0.1)",
                "sin(100)",
                "cos(100)",
                "tan(2)",
                "cot(2)",
                "asin(0.1)",
                "acos(0.1)",
                "atan(0.1)",
                "acot(0.1)",
                "tanh(2)",
                "asinh(0.1)",
                "acosh(2)",
                "acoth(2)",
                "atanh(0.1)",
                "coth(2)"));

        aggregateCsv.run("very slow functions performance over releases.csv", "../%s/performance.csv", releaseNames, Arrays.asList(
                "asin(0.8)",
                "atan2(2;3)",
                "gamma(0.1)"));
    }
}
