package huawei_nlp.utils;

import huawei_nlp.Cell;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public List<Integer> getClassAvailability(Integer... integers) {
        return Arrays.stream(integers).collect(Collectors.toList());
    }

    public List<Cell> getInitialModel(String path) {
        List<Cell> result = new ArrayList<>();
        try {
            Files.readAllLines(Paths.get(path), Charset.defaultCharset()).forEach(string -> result.add(new Cell(result.size(), string)));
            return result;
        } catch (IOException ex) {
            return null;
        }
    }



    public String createHypothesis(String path, List<Cell> model) {
        List<String> hypothesisComponents = new ArrayList<>();
        try {
            List<String> data = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
            data.forEach(str -> {
                hypothesisComponents.add(model.stream().filter(cell -> cell.getKey().equals(str)).findFirst().orElse(null).getToxicity().toString() + "\n");
            });
            return String.join("", hypothesisComponents);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (NullPointerException ex) {
            ex.getCause();
            ex.printStackTrace();
            return "";
        }
    }

    public void waitLoading(Integer timeoutMillis) {
        try {
            Thread.sleep(timeoutMillis);
        } catch (InterruptedException ex) {
        }
    }
}
