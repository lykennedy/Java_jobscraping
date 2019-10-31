# Java_jobscraping
I had a lot of fun with this program. I already worked with selenium and requests in Python but I have never worked with Selenium in Java.
Turns out the functions are basically the same, so I had a really easy time picking this up. 
This program uses Selenium to go to two sites: Indeed and Linkedin. The program will scrape up all the job data (title, company, location, link) and places it into an csv file.
There are two functions, Indeed() and Linkedin(), both instance methods inside of a class named Browser that will go to either of the two sites. So with that being said, a Browser class must first be created then call those two functions to begin.
Selenium will then grab all the data and place everything it found into an csv file using the function Excel() with a parameter of all the job postings scraped earlier, that is stored in a field named postings.
Go to where the java file is located and boom, there should be a new csv file in there for you to click. Once that has opened, you can see that it includes the titles, company, location, and links all in one place.
Note, in order for the Linkedin function to work, you must pass in the Username and password of your linkedin profile. This is due to Linkedin's login authentication.
Another note, for this specific program, I initiated the object like this Browser browser = new Browser("Software Engineer", "Indianapolis"). Those parameters can be changed to a person's liking. The first argument is just the job title the person is looking for. The second argument is the location, change accordingly.
