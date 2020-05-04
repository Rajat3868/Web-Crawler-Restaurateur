import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.util.concurrent.TimeUnit;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import org.awaitility.Awaitility;

public class GetRestIds {

    private File src = new File("D:\\Zomato\\Chennai.xlsx");
    private FileInputStream fis = new FileInputStream(src);
    private XSSFWorkbook wb = new XSSFWorkbook(fis);
    private XSSFSheet sh1 = wb.getSheetAt(0);
    private WebDriver driver = new ChromeDriver();
    private Integer pg;

    public GetRestIds() throws Exception {

    }

    @Test()
    public void webScrap() throws Exception {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();


        PrintStream out = new PrintStream(new FileOutputStream("/D:/Zomato/punerestids.html"));
        String city = "pune";

        driver.get("https://www.zomato.com/"+city+"/restaurants?page=1");
        String pages = String.valueOf(driver.findElement(By.xpath("//*[@id=\"search-results-container\"]/div[2]/div[1]/div[1]/div/b[2]")).getText());

        pg = Integer.valueOf(pages);
        System.out.println("pages to scrap: "+pg);


        for (int i = 1; i <= pg; i++) {

            driver.get("view-source:https://www.zomato.com/"+city+"/restaurants?page=" + i);
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            String id1 = driver.findElement(By.xpath("/html/body/table/tbody/tr[220]/td[2]")).getText();

            String ids = id1.replaceAll("[a-z]", "")
                    .replaceAll("[_':{\"}]", "")
                    .replaceAll("[,]", "\n")
                    .replace("[", "")
                    .replace("]", "")
                    .trim();
            System.setOut(out.append("\n").append(ids));
            Thread.sleep(3000);
        }
    driver.quit();
    }


    @Test()
    public void prepareExcel () throws Exception{

//        int est_id_count = pg*15;
        String[] elem_x = new String[9000];
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        driver.get("view-source:D:\\Zomato\\kolkatarestids.html");
        sh1.createRow(0).createCell(0).setCellValue("Restaurant IDs");


        for (int j=1; j<=8000; j++){

            elem_x[j] = driver.findElement(By.xpath("/html/body/table/tbody/tr["+(j+1)+"]/td[2]")).
                    getText();
            if (elem_x[j].equals("===============================================")) {
                System.out.println(j+" done");
                break;
            }
            else {
                sh1.createRow(j).createCell(0).setCellValue(elem_x[j]);
            }
        }

        FileOutputStream fout = new FileOutputStream(src);
        wb.write(fout);

        driver.quit();
    }


    @Test()
    public void writeExcel() throws Exception{
        driver.quit();

        String resid;

        String user_key1 = "60cf3c0bae85ab0eda1cfcd98f8a7c43";
        String user_key2 = "ff6c6eb2d1afc2f86eb352aaee1c27e5";
        String user_key3 = "c732e2e45b9f2748720fb358a0ab04c2";

        String user_key;

//        sh1.getRow(0).createCell(1).setCellValue("Restaurant Links");
//        sh1.getRow(0).createCell(2).setCellValue("Restaurant Name");
//        sh1.getRow(0).createCell(3).setCellValue("Rating");
//        sh1.getRow(0).createCell(4).setCellValue("Votes");
//        sh1.getRow(0).createCell(5).setCellValue("Locality");
//        sh1.getRow(0).createCell(6).setCellValue("Address");
//        sh1.getRow(0).createCell(7).setCellValue("Cost for Two People");
//        sh1.getRow(0).createCell(8).setCellValue("Contact Numbers");
//        sh1.getRow(0).createCell(9).setCellValue("Cuisines");

//        int id_count = sh1.getLastRowNum();


        for (int k=1; k<=2000; k++){
            try {

                resid = sh1.getRow(k).getCell(0).getStringCellValue();
                user_key = user_key2;


                Response rest_details = given().
                        header("user-key", user_key).
                        queryParam("res_id", resid).
                        get("https://developers.zomato.com/api/v2.1/restaurant");

                Awaitility.await().atMost(10, TimeUnit.SECONDS).
                        with().
                        pollInterval(1, TimeUnit.SECONDS);

                int status = rest_details.getStatusCode();

                if (status == 200) {

                    Object rating = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("user_rating.aggregate_rating");
                    if (rating instanceof String) {

                        String rating_to_use = rest_details.then().
                                contentType(ContentType.JSON).
                                extract().
                                path("user_rating.aggregate_rating");
                        sh1.getRow(k).createCell(3).setCellValue(rating_to_use);
                    } else {
                        Integer rating_to_use = ((Number) rating).intValue();
                        sh1.getRow(k).createCell(3).setCellValue(String.valueOf(rating_to_use));
                    }


//                if (Integer.parseInt(String.valueOf(rating))>=4){


                    String loc_url = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("url");

                    String loc_actual_url = loc_url.replaceAll("\\?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1", "")
                            .replace("?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1", "");

                    String name = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("name");

//
//                Object votes = rest_details.then().
//                            contentType(ContentType.JSON).
//                            extract().
//                            path("all_reviews_count");
//                if (votes instanceof Integer){
////                    Integer votes_to_use = ((Number)votes).intValue();
//                    sh1.getRow(k).createCell(4).setCellValue("0");
//                }
//                else {
//                    String votes_to_use = rest_details.then().
//                            contentType(ContentType.JSON).
//                            extract().
//                            path("user_rating.aggregate_rating");
//                    sh1.getRow(k).createCell(4).setCellValue(votes_to_use);
//                }

                    String locality = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("location.locality");

                    String address = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("location.address");

                    Integer cost_for_two = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("average_cost_for_two");

                    String contact_number = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("phone_numbers");

                    String cuisines = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("cuisines");

                    String establishment = rest_details.then().
                            contentType(ContentType.JSON).
                            extract().
                            path("establishment").toString().replace("]","").replace("[", "");


                    sh1.getRow(k).createCell(1).setCellValue(loc_actual_url);
                    sh1.getRow(k).createCell(2).setCellValue(name);
                    sh1.getRow(k).createCell(5).setCellValue(locality);
                    sh1.getRow(k).createCell(6).setCellValue(address);
                    sh1.getRow(k).createCell(7).setCellValue(String.valueOf(cost_for_two));
                    sh1.getRow(k).createCell(8).setCellValue(contact_number);
                    sh1.getRow(k).createCell(9).setCellValue(cuisines);
                    sh1.getRow(k).createCell(10).setCellValue(establishment);


                    System.out.println(k + " rows done");

                    Thread.sleep(3000);
//                }
//                else sh1.getRow(k).createCell(1).setCellValue("Less than 4 rating");

                } else if (status == 404) {
                    System.out.println("Error in API response: " + status + " Restaurant ID: " + resid);
                    sh1.getRow(k).createCell(1).setCellValue("Error 404");
                    Thread.sleep(2000);
                } else {
                    System.out.println("Error in API response: " + status);
                    break;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                break;
            }


        }
        FileOutputStream fout = new FileOutputStream(src);
        wb.write(fout);

    }
}
