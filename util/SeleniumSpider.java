package util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by coral on 2017/7/18.
 */
public class SeleniumSpider implements Serializable{
    int networkTryTimes = 2; // 没有下一页会尝试3次
    WebDriver driver;
    public SeleniumSpider(){
        System.setProperty("webdriver.chrome.driver",
                "d:/qq/chromedriver.exe");
        //                "/home/ubuntu/software/selenium/chromedriver.exe");

    }

    public Set<String> getUrls(String url, String nextPage, String urlXpath) throws Exception {
        driver = new ChromeDriver();

        Set<String> urlSet = new HashSet<>();
        driver.get(url);
        Thread.sleep(5000);
        WebElement more;

        int page = 0;

        while (page < 30) {
            System.out.println("========================page number is : ====" + page++);
            //more = driver.findElement(By.xpath("//*[@id=\"instantPanel\"]/div[9]/a[8]"));//*[@id="instantPanel"]/div[9]/a[9]

            List<WebElement> list = null;//从数据库获取xpath
            try {
                list = (driver.findElements(By.xpath(urlXpath)));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("=============================wrong xpath : urls & xapth is : " + url +"  \n" + urlXpath );
                break;
            }
            for (WebElement element : list) {
                try {
                    String u = element.getAttribute("href");
                    urlSet.add(u);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            //网络异常可能会有很多问题。 服务器端- 网络- 本地故障： 太容易出现问题了
            //try 3 times, if still no nextpage, return
//            for (int k = 0; k < networkTryTimes; k++) {
//                try {
//                    //tencent has ">"
//                    more = driver.findElement(By.linkText(nextPage));
//                    more.click();
//                    break;
//                } catch (NoSuchElementException e) {
//                    Thread.sleep(1000);
//                    e.printStackTrace();
//                    continue;
//                }finally {
//                    //******** TODO: 2017/7/18  break still finally !!!
//                    if(k == networkTryTimes-1 ){
//                        //没有下一页了
//                        driver.close();
//                        return urlSet;
//                    }
//                }
//
//            }

            try {
                //tencent has ">"
                more = driver.findElement(By.linkText(nextPage));
                more.click();
            } catch (NoSuchElementException e) {
                System.out.println("no more nextpage");
                Thread.sleep(1000);
                e.printStackTrace();
                break;
            }
            Thread.sleep(1000);
        }

        try {
            driver.close();
        } catch (Exception e) {
            System.out.println("driver close exception");
            e.printStackTrace();
        }
        return urlSet;
    }
}
