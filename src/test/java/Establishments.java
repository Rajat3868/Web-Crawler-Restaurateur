import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Establishments {

    private File src = new File("C:\\Users\\User\\Desktop\\Zomato Restaurant Data.xlsx");
    private FileInputStream fis = new FileInputStream(src);
    private XSSFWorkbook wb = new XSSFWorkbook(fis);
    private XSSFSheet sh1 = wb.getSheetAt(0);

    public Establishments() throws Exception {
    }

    @Test
    public void estabs () throws Exception{

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 5);


        int total_rest = sh1.getLastRowNum();
        System.out.println("No. of rows: " +total_rest);


        for (int i = 1; i < total_rest; i++) {
                try {

                    String out_link = sh1.getRow(i).getCell(0).getStringCellValue();
                    driver.get(out_link+"/info");
                    String title = driver.getTitle();
                    if (!title.contains("404"))
//                    System.out.println(title);
                        try{
                            WebElement a = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='grey-text fontsize3']")));
                            String est = driver.findElement(By.xpath("//a[@class='grey-text fontsize3']")).getText();
                            sh1.getRow(i).createCell(11).setCellValue(est);
//                            System.out.println(i + ". Establishment type: " + est);
                          }
                        catch (Exception f){
                            System.out.println(i+ " element not found, do manual");
                        }
                    else System.out.println("404");

                        Thread.sleep(2000);

            }
        catch (Exception e) {
            e.printStackTrace();
            break;
        }

        }
        FileOutputStream fout = new FileOutputStream(src);
        wb.write(fout);

    }


}
