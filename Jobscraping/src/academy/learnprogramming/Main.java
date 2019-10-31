package academy.learnprogramming;



import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Browser browser = new Browser("Software Engineer", "Indianapolis");
        browser.Linkedin("kennedyly01@gmail.com", "Sniffy69");
        browser.Indeed();
        System.out.println(browser.getPostings());
        //System.out.println(browser.getPostings().size());
        browser.Excel(browser.getPostings());
//        String str = "Fast Enterprises, LLC";
//        System.out.println(str.contains(","));
//        str = str.replace(",", "");
//        System.out.println(str);
    }
}
