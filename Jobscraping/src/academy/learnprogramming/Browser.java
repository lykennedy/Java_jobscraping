package academy.learnprogramming;


import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Browser {
    private String Job_Title;
    private String Location;
    private ArrayList<ArrayList<String>> postings = new ArrayList<ArrayList<String>>(); //Will hold all the jobs scraped.
    public Browser(String Job_Title, String Location)

    {
        this.Job_Title = Job_Title;
        this.Location = Location;
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kennedy\\Downloads\\chromedriver.exe");

    }

    public void Indeed() throws IOException {
        int i;
        WebDriver browser = new ChromeDriver();
        browser.get("https://indeed.com");
        browser.findElement(By.id("text-input-what")).sendKeys(this.Job_Title); // Finds the input form for the job title.
        WebElement location = browser.findElement(By.id("text-input-where")); // The clear function returns void, so alt step. finding element returns a webelement object.
        for (i=0;i<62;i++) // Brute force our way in. Max characters location can have is 62.
        {
            location.sendKeys(Keys.BACK_SPACE);
        }
        location.sendKeys(this.Location);
        location.sendKeys(Keys.ENTER); //Going on the results page

        List<WebElement> jobs = new ArrayList<>();
        jobs = browser.findElements(By.className("jobsearch-SerpJobCard")); //Job listings into a list.

        for (i=0; i<jobs.size(); i++)
        {
            String title = jobs.get(i).findElement(By.className("title")).getText(); //This is the only text inside the job posting so this works.
            String company= "";
            company = catching_company(company, i, jobs);

            String Location = "";
            Location = catching_location(Location, i, jobs);

            String link = jobs.get(i).findElement(By.className("title")).findElement(By.tagName("a")).getAttribute("href");

            company = comma_check(company);
            Location = comma_check(Location);
            title = comma_check(title);

            System.out.println(title);
            System.out.println(company);
            System.out.println(Location);
            System.out.println(link);
            System.out.println();
            ArrayList<String> rows = new ArrayList<String>(); //Put everything together for easier appending to the excel.
            rows.add(title);
            rows.add(company);
            rows.add(Location);
            rows.add(link);
            int j;
            for (j=0; j<rows.size(); j++) //Trimming any white space present
            {
                rows.set(j, rows.get(j).trim());
            }
            this.postings.add(rows);

        }
        browser.close();
        browser.quit();
    }

    public void Linkedin(String Username, String Password) throws InterruptedException {
        WebDriver browser = new ChromeDriver();
        browser.get("https://www.linkedin.com/");
        browser.findElement(By.xpath("/html/body/nav/a[3]")).click();
        browser.findElement(By.id("username")).sendKeys(Username);
        browser.findElement(By.id("password")).sendKeys(Password);
        browser.findElement(By.xpath("//*[@id=\"app__container\"]/main/div/form/div[3]/button")).click();
        Thread.sleep(3000);
        browser.findElement(By.id("jobs-tab-icon")).click();
        Thread.sleep(3000);
        String job_id = browser.findElement(By.className("jobs-search-box__input")).findElement(By.tagName("input")).getAttribute("id"); //Everything is dynamically changing so this will grab the id.
        browser.findElement(By.id(job_id)).sendKeys(this.Job_Title);
        int ember = Integer.parseInt(job_id.replaceAll("\\D", "")); // Takes out the id number from the job_id calculated earlier
        // The id number for both input fields are the same, taking advantage of this fact.
        browser.findElement(By.id("jobs-search-box-location-id-ember" + ember)).sendKeys(this.Location);
        browser.findElement(By.id("jobs-search-box-location-id-ember" + ember)).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        List<WebElement> test = browser.findElement(By.className("jobs-search-results")).findElement(By.tagName("ul")).
                findElements(By.className("occludable-update"));
        System.out.println(test.size());

        int i;
        Actions action = new Actions(browser);
        for (i=0;i<test.size();i++)
        {
            ArrayList<String> positions  = new ArrayList<String>();

            if (i % 2 == 0) //All elements aren't in DOM yet so this allows for all to load.
            {
                action.moveToElement(test.get(i).findElement(By.className("job-card-search__title")));
                action.perform();
                action.click();
                action.perform();
            }

            String title = test.get(i).findElement(By.className("job-card-search__title")).findElement(By.tagName("a")).getText();


            if (title.contains("Promoted"))
            {
                title = replace(title);
            }
            String company = test.get(i).findElement(By.tagName("h4")).getText();
            String location = test.get(i).findElement(By.className("job-card-search__location")).getText();
            String link = test.get(i).findElement(By.tagName("h3")).findElement(By.tagName("a")).getAttribute("href");

            company = comma_check(company);
            location = comma_check(location);
            title = comma_check(title);

            positions.add(title);
            positions.add(company);
            positions.add(location.replace(",", ""));
            positions.add(link);
            System.out.println(title);
            System.out.println(company);
            System.out.println(location);
            System.out.println(link);
            System.out.println();
            this.postings.add(positions);
        }
//        ((JavascriptExecutor)browser).executeScript("arguments[0].scrollIntoView();" ,browser.findElement(By.className("jobs-search-two-page__pagination")));
//        List<WebElement> pages = browser.findElement(By.className("jobs-search-two-page__pagination")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
//        action.moveToElement(pages.get(1));
//        action.perform();
//        action.click();
//        action.perform(); //Attempting to scroll down for the page numbers. Failed :(
        browser.close();
        browser.quit();

    }

    public void Excel(ArrayList<ArrayList<String>> rows) throws IOException {
        FileWriter excel = new FileWriter("jobs.csv");
        excel.append("Title"); //Creating the headers
        excel.append(","); //Goes onto the next cell

        excel.append("Company");
        excel.append(",");

        excel.append("Location");
        excel.append(",");

        excel.append("Link");
        excel.append("\n"); //End of the headers

        for (List<String> row : rows)
        {
            //excel.append(String.join(",", row));
            excel.append(String.join(",", row));
            excel.append("\n");
        }


        excel.flush();
        excel.close();
    }

    private static String catching_company(String company, int i, List<WebElement>jobs) //Used for indeed
    {
        try
        {
            company = jobs.get(i).findElement(By.className("sjcl")).findElement(By.tagName("span")).getText();
            return company;
        }
        catch (Exception e)
        {
            return "N/A";
        }
    }

    private static String catching_location(String location, int i, List<WebElement>jobs) //Used for indeed
    {
       try
        {
            location = jobs.get(i).findElement(By.className("sjcl")).findElement(By.className("location")).getText();
            return location;
        }
        catch (Exception e)
        {
            return "N/A";
        }
    }

    private static String replace(String title) //Some linked in jobs have promoted in their listing which is not part of the job title. Get rid of it.
    {
        String temp = " " + "Promoted";
        title = title.replaceAll(temp, "");
        return title;
    }

    private static String catching_title(String title, int i, List<WebElement>test)
    {
        try
        {
            title = test.get(i).findElement(By.tagName("h3")).findElement(By.tagName("a")).getText();
            return title;
        }
        catch (Exception e)
        {
            return "N/A";
        }
    }

    public ArrayList<ArrayList<String>> getPostings() {
        return postings;
    }

    private static String comma_check(String thing)
    {
        if (thing.contains(","))
        {
            thing = thing.replace(",", "");
        }
        return thing;
    }
}
