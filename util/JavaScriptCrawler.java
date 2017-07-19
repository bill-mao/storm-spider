package util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;


public class JavaScriptCrawler {
    public static void main(String[] args) throws Exception {



        System.setProperty("webdriver.chrome.driver",
                "d:/qq/chromedriver.exe");
//                "/home/ubuntu/software/selenium/chromedriver.exe");

        WebDriver driver = new ChromeDriver();

//        driver.get("http://roll.ent.qq.com/");
        driver.get("http://roll.auto.qq.com/index.htm?site=auto&mod=1&date=2017-07-13&cata=#");
        Thread.sleep(5000);
        WebElement more = null;
        int i = 0;
        // while(true){
        while (true) {
            System.out.println("========================page number is : ====" + i++);
            //more = driver.findElement(By.xpath("//*[@id=\"instantPanel\"]/div[9]/a[8]"));//*[@id="instantPanel"]/div[9]/a[9]
            List<WebElement> lw = new ArrayList<WebElement>();
            lw = driver.findElements(By.xpath("//div[@id=\"artContainer\"]/ul/li/a"));//从数据库获取xpath
            //*[@id="artContainer"]/ul[8]/li[1]/a  /ul/li[1]/a //*[@id="artContainer"]/ul[1]/li[2]/a
            for (WebElement element : lw) {
                try {
                    System.out.println(element.getText());
                    System.out.println(element.getAttribute("href"));//获取到链接，添加数据库操作
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            //网络异常可能会有很多问题。 服务器端- 网络- 本地故障： 太容易出现问题了

            for(int k =0; k< 3; k++){
                try {
                    //tencent has ">"
//                    more = driver.findElement(By.linkText("下一页"));
                    more = null;
                    more = driver.findElement(By.linkText("下一页>"));//直接获取的classname
                    more.click();
                    break;
                }
                catch (NoSuchElementException ne){
                    ne.printStackTrace();
                    System.out.println("=============================" + more == null);
                    return;
                }
                catch (Exception e) {
                    Thread.sleep(1000);
                    e.printStackTrace();
                    continue;
                }
            }

            Thread.sleep(3000);
        }
        //System.out.println(more.getText());
        //}
         /*  more.click();
         Thread.sleep(5000); // Let the user actually see something!
         List<WebElement> names = driver.findElements(By.cssSelector("table tbody tr td h4 a"));
         for(WebElement e : names) {
             System.out.println(e.getText());  */

        // }
        // Thread.sleep(5000); // Let the user actually see something!
        //driver.quit();
         /*  WebDriver driver = new ChromeDriver();
         ChromeOptions options = new ChromeOptions();
         options.addArguments("--start-maximized");
         driver.get("http://www.sina.com");
       //  Thread.sleep(5000);
         WebElement more = driver.findElement(By.cssSelector("p.showmore a"));
         more.click();
         Thread.sleep(5000); // Let the user actually see something!
         List<WebElement> names = driver.findElements(By.cssSelector("table tbody tr td h4 a"));
         for(WebElement e : names) {
             System.out.println(e.getText());
         }
         Thread.sleep(5000); // Let the user actually see something!
         driver.quit();
    	 /*
         //等待数据加载的时间
         //为了防止服务器封锁，这里的时间要模拟人的行为，随机且不能太短
         long  waitLoadBaseTime =  3000 ;
         int  waitLoadRandomTime =  3000 ;
         Random random =  new  Random(System.currentTimeMillis());

         //火狐浏览器
         WebDriver driver =  new  ChromeDriver();
         //要抓取的网页
         driver.get( "http://toutiao.com/" );

         //等待页面动态加载完毕
         Thread.sleep(waitLoadBaseTime+random.nextInt(waitLoadRandomTime));

         //要加载多少页数据
         int  pages= 5 ;
         for ( int  i= 0 ; i<pages; i++) {
             //滚动加载下一页
             driver.findElement(By.className( "loadmore" )).click();
             //等待页面动态加载完毕
             Thread.sleep(waitLoadBaseTime+random.nextInt(waitLoadRandomTime));
         }

         //输出内容
         //找到标题元素
         List<WebElement> elements = driver.findElements(By.className( "title" ));
         int  j= 1 ;
         for ( int  i= 0 ;i<elements.size();i++) {
             try  {
                 WebElement element = elements.get(i).findElement(By.tagName( "a" ));
                 //输出标题
                 System.out.println((j++) +  "、"  + element.getText() +  " "  + element.getAttribute( "href" ));
             } catch  (Exception e){
                 System.out.println( "ignore " +elements.get(i).getText()+ " because " +e.getMessage());
             }
         }

         //关闭浏览器
         driver.close();*/
    }
}