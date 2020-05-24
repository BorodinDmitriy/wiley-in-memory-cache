package huawei_nlp;

import huawei_nlp.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NLPAssignmentTest {

    public static WebDriver webDriver;
    public static WebDriverWait wait;
    public static Integer timeout = 10;
    public static String login = "";
    public static String password = "";
    public static TestUtils testUtils = new TestUtils();

    public static List<Integer> classAvailability = testUtils.getClassAvailability(4331, 1781, 1088, 1373, 504, 117);
    public static List<Cell> model = getInitialModel("src/main/resources/reply/dataset.txt", "src/main/resources/reply/reply.txt");
    public static Double currentAccuracy = 0.7942136175766804; // точность для единичек

    public static List<Cell> getInitialModel(String pathData, String pathReply) {
        List<Cell> result = new ArrayList<>();
        try {
            List<String> replys = Files.readAllLines(Paths.get(pathReply), Charset.defaultCharset());
            Files.readAllLines(Paths.get(pathData), Charset.defaultCharset()).forEach(string -> {
                Integer toxicity = Integer.parseInt(replys.get(result.size()));
                if (toxicity > 0) {
                    classAvailability.set(toxicity, classAvailability.get(toxicity) - 1);
                }
                result.add(new Cell(result.size(), string, toxicity));
            });
            return result;
        } catch (IOException ex) {
            return null;
        }
    }

    @BeforeAll
    static void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver");
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        String baseUrl = "https://stepik.org/catalog?auth=login";
        String targetUrl = "https://stepik.org/lesson/316011/step/2?unit=312237";
        webDriver.get(baseUrl);
        wait = new WebDriverWait(webDriver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"id_login_email\"]")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"id_login_password\"]")));
        webDriver.findElement(By.xpath("//*[@id=\"id_login_email\"]")).sendKeys(login);
        webDriver.findElement(By.xpath("//*[@id=\"id_login_password\"]")).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"login_form\"]/button")));
        webDriver.findElement(By.xpath("//*[@id=\"login_form\"]/button")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"ember579\"]")));
        webDriver.get(targetUrl);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ember137")));
        JavascriptExecutor jse = (JavascriptExecutor) webDriver;
        jse.executeScript("window.scrollBy(0,750)");
        try {
            Thread.sleep(7000);
        } catch (InterruptedException ex) {
        }
        jse.executeScript("window.scrollBy(0,1200)");
    }

    @Test
    void test() {

        model.forEach(cell -> {
            for (int i = 1; i <= 5; i++) {
                if (cell.getFixed()) {
                    break;
                }
                if (classAvailability.get(i) <= 10) {
                    break;
                }
                if (currentAccuracy > 0.9) {
                    break;
                }
                cell.setToxicity(i);
                Double accuracy = getAccuracy();
                if (accuracy < currentAccuracy) {
                    // единица - целевая токсичность
                    cell.setToxicity(0);
                    cell.setFixed(true);
                    classAvailability.set(0, classAvailability.get(0) - 1);
                    System.out.println("id = " + cell.getId() + " tx = " + cell.getToxicity() + " " +  classAvailability.get(0) + " " +
                            classAvailability.get(1) + " " +
                            classAvailability.get(2) + " " +
                            classAvailability.get(3) + " " +
                            classAvailability.get(4) + " " +
                            classAvailability.get(5));
                }
                if (accuracy > currentAccuracy) {
                    // нашли новую токсичность
                    currentAccuracy = accuracy;
                    cell.setToxicity(i);
                    cell.setFixed(true);
                    classAvailability.set(i, classAvailability.get(i) - 1);
                    System.out.println("id = " + cell.getId() + " tx = " + cell.getToxicity() + " " + classAvailability.get(0) + " " +
                            classAvailability.get(1) + " " +
                            classAvailability.get(2) + " " +
                            classAvailability.get(3) + " " +
                            classAvailability.get(4) + " " +
                            classAvailability.get(5) + " acc = " + currentAccuracy + " time = " + LocalDateTime.now().toString());
                }

            }
        });
        model.forEach(cell -> {
            if (!cell.getFixed()) {
                cell.setToxicity(0);
                cell.setFixed(true);
            }
        });
        Double accuracy = getAccuracy();
    }

    private Double getAccuracy() {
        String path = "/home/dmitriy/Загрузки/dataset_316011_2.txt";
        String reply = "/home/dmitriy/Загрузки/reply_316011_2.txt";
        Integer unknownElements = 0;
        String againBtnXpath = "//*[@id=\"ember668\"]/div/section/div/div[2]/div[3]/button";
        String answerXpath = "//*[@id=\"ember285\"]";
        String className = "again-btn";
        String classNameDowloadData = "get_dataset";
        String downloadDataXPath = "//*[@id=\"ember2737\"]/div/a";
        String classNameTextArea = "autoresize-textarea";
        String classNameUploadFile = "fileupload__input";
        String classNameHint = "smart-hints__hint";
        String hintXPath = "//*[@id=\"ember266\"]/pre";
        String fullHintXPath = "/html/body/div/div[1]/div[2]/main/div/div[3]/div[2]/div[2]/div/article/div/div/div[2]/div/section/div/div[1]/div/pre";
        String classSubmit = "submit-submission";
        try {
            testUtils.waitLoading(5000);
            wait.until(ExpectedConditions.elementToBeClickable(By.className(className)));
            webDriver.findElement(By.className(className)).click();
            testUtils.waitLoading(10000);
            wait.until(ExpectedConditions.elementToBeClickable(By.className(classNameDowloadData)));
            webDriver.findElement(By.className(classNameDowloadData)).click();
            do {
                testUtils.waitLoading(500);
            } while (!Files.exists(Paths.get(path)));

            String hypothesis = testUtils.createHypothesis(path, model);
            StringSelection selection = new StringSelection(hypothesis);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            wait.until(ExpectedConditions.elementToBeClickable(By.className(classNameTextArea)));
            webDriver.findElement(By.className(classNameTextArea)).sendKeys(Keys.CONTROL + "v");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.className(classSubmit)));
            wait.until(ExpectedConditions.elementToBeClickable(By.className(classSubmit)));
            webDriver.findElement(By.className(classSubmit)).click();
            Double accuracy = 0.0;
            do {
                testUtils.waitLoading(500);
                try {
                    wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.className(classNameHint))));
                    accuracy = Double.valueOf(webDriver.findElement(By.className(classNameHint)).getText());
                } catch (Exception e) {
                    continue;
                }

            } while (accuracy == 0.0);

            File fileToDelete = new File(path);
            if (fileToDelete.delete()) {

            }
            return accuracy;
        } catch (Exception ex) {
            return currentAccuracy;
        }

    }

    @AfterAll
    static void exit() {
        webDriver.close();
    }
}
