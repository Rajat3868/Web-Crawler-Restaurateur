//import com.jayway.restassured.http.ContentType;
//import com.jayway.restassured.response.Response;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.annotations.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Arrays;
//
//import static com.jayway.restassured.RestAssured.given;
//
//public class ExcelRead {
//
//    private File src = new File("D:\\Zomato\\final2.xlsx");
//    private FileInputStream fis = new FileInputStream(src);
//    private XSSFWorkbook wb = new XSSFWorkbook(fis);
//    private XSSFSheet sh1 = wb.getSheetAt(0);
//
//    public ExcelRead() throws Exception {
//    }
//
//    @Test
//    public void RenderIds () throws Exception{
//
//
//        String[] elem_x = new String[200];
//        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//        driver.get("view-source:D:\\Zomato\\finaltest.html");
//
//
//        sh1.createRow(0).createCell(0).setCellValue("Restaurant IDs");
//
//    for (int j=1; j<=200; j++){
//
//        elem_x[j] = driver.findElement(By.xpath("/html/body/table/tbody/tr["+(j+1)+"]/td[2]")).
//            getText();
//        if (elem_x[j].equals("===============================================")) {
//            break;
//        }
//        else {
//            sh1.createRow(j).createCell(0).setCellValue(elem_x[j]);
//        }
//        }
//
//        FileOutputStream fout = new FileOutputStream(src);
//        wb.write(fout);
//
//        driver.quit();
//    }
//
//
//    @Test
//    public void Details() throws Exception{
//
//        String resid;
//
//        String user_key = "ff6c6eb2d1afc2f86eb352aaee1c27e5";
//
//        for (int k=1; k<150; k++){
//
//            resid=sh1.getRow(k).getCell(0).getStringCellValue();
//
//            Response rest_details =
//                    given().
//                            header("user-key", user_key).
//                            queryParam("res_id",resid).
//                            get("https://developers.zomato.com/api/v2.1/restaurant");
//
//            String loc_url = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("url");
//
//            String loc_actual_url = loc_url.replaceAll("\\?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1", "")
//                    .replace("?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1", "");
//
//            String name = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("name");
//
//            String rating = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("user_rating.aggregate_rating");
//
//            Integer votes = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("all_reviews_count");
//
//            String locality = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("location.locality");
//
//            String address = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("location.address");
//
//            Integer cost_for_two = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("average_cost_for_two");
//
//            String contact_number = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("phone_numbers");
//
//            String cuisines = rest_details.then().
//                    contentType(ContentType.JSON).
//                    extract().
//                    path("cuisines");
//
//
//
//            sh1.getRow(k).createCell(1).setCellValue(loc_actual_url);
//            sh1.getRow(k).createCell(2).setCellValue(name);
//            sh1.getRow(k).createCell(3).setCellValue(String.valueOf(rating));
//            sh1.getRow(k).createCell(4).setCellValue(String.valueOf(votes));
//            sh1.getRow(k).createCell(5).setCellValue(locality);
//            sh1.getRow(k).createCell(6).setCellValue(address);
//            sh1.getRow(k).createCell(7).setCellValue(String.valueOf(cost_for_two));
//            sh1.getRow(k).createCell(8).setCellValue(contact_number);
//            sh1.getRow(k).createCell(9).setCellValue(cuisines);
//
//
//        }
//        FileOutputStream fout = new FileOutputStream(src);
//        wb.write(fout);
//
//    }
//
//
//}
