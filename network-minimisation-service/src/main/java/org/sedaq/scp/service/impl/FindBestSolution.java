package org.sedaq.scp.service.impl;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Pavel Seda
 */
@Service
public class FindBestSolution {

    public void findTheBestResult(Path resultsBasePath) {
        try (Stream<Path> paths = Files.walk(resultsBasePath)) {
            List<Path> results = paths
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().toLowerCase().endsWith("results-info.txt"))
                    .collect(Collectors.toCollection(ArrayList::new));

            Path theBestResultsDirectory = resultsBasePath.resolve(Paths.get("the-best-result-from-run"));
            Files.createDirectories(theBestResultsDirectory);

            Long theLowestNumberOfNecessaryServices = Long.MAX_VALUE;
            Path theBestResultAlgorithmIterations = null;
            Path theBestResultInfo = null;
            for (int i = 0; i < results.size(); i++) {
                try {
                    BufferedReader br = Files.newBufferedReader(results.get(i));
                    String firstLine = br.readLine();
                    long numberOfNecessaryServices = Long.parseLong(firstLine.split(":")[1].trim());
                    if (numberOfNecessaryServices < theLowestNumberOfNecessaryServices) {
                        theLowestNumberOfNecessaryServices = numberOfNecessaryServices;
                        theBestResultInfo = results.get(i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Path theBestResultsInfoPath = theBestResultsDirectory.resolve(theBestResultInfo.getFileName());
            Files.copy(theBestResultInfo, theBestResultsInfoPath, StandardCopyOption.COPY_ATTRIBUTES);

            // find the algorithm iteration file and copy it also to the the-best-result-from-run folder
            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(theBestResultsInfoPath.getFileName().toString());
            if (matcher.find()) {
                String algorithmIterationsTimestamp = matcher.group(1);
                List<Path> algorithmIterations = Files.walk(resultsBasePath)
                        .filter(p -> p.toString().toLowerCase().endsWith(".csv"))
                        .filter(p -> p.getFileName().toString().startsWith(algorithmIterationsTimestamp))
                        .collect(Collectors.toCollection(ArrayList::new));
                if (algorithmIterations != null && algorithmIterations.size() > 0) {
                    // find particular file and rename it to algname-results.csv
                    Pattern p = Pattern.compile("(\\d+)-(\\w+-\\w+)");
                    String str = algorithmIterations.get(0).getFileName().toString();
                    Matcher m = p.matcher(str);
                    if (m.find()) {
                        Path theBestResultsAlgorithmsIterationsPath = theBestResultsDirectory.resolve(Paths.get(m.group(2) + ".csv"));
                        Files.copy(algorithmIterations.get(0), theBestResultsAlgorithmsIterationsPath, StandardCopyOption.COPY_ATTRIBUTES);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
