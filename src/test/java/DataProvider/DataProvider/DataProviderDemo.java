package DataProvider.DataProvider;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;

public class DataProviderDemo {
	public WebDriver driver;
	CSVReader csvReader;
	private static final String SAMPLE_CSV_FILE_PATH = "C:\\Users\\amisharm25\\Desktop\\data.csv";

	@BeforeClass
	public void setUp() throws Exception {
		Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
		csvReader = new CSVReader(reader);
		String path = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", path + "\\resources\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://automationpractice.com");
		WebElement signIn = driver.findElement(By.cssSelector("a.login"));
		highLightElement(signIn);
		signIn.click();
		

	}

	@Test(dataProvider = "getData")
	public void verifyLogin(String username, String passsword) throws Exception {
	
		Actions action=new Actions(driver);
		action.sendKeys(Keys.PAGE_DOWN).build().perform();
		System.out.println("username is" + username);
		System.out.println("password is" + passsword);

		WebElement email = driver.findElement(By.id("email"));
		highLightElement(email);
		email.clear();
		email.sendKeys(username);

		WebElement Password = driver.findElement(By.id("passwd"));
		highLightElement(Password);
		Password.clear();
		Password.sendKeys(passsword);

		WebElement login = driver.findElement(By.id("SubmitLogin"));
		highLightElement(login);

		login.click();
		Thread.sleep(2000);

	}

	@DataProvider(name = "getData")
	public Object[][] getData() throws Exception {

		List<String[]> records = csvReader.readAll();

		Object[][] array = null;

		for (int i = 0; i < records.size(); i++) {

			Object[] row = records.get(i);
			if (Objects.isNull(array)) {
				array = new Object[records.size()][row.length];
			}
			array[i][0] = row[0];
			// System.out.println(array[i][column]);
			array[i][1] = row[1];
			// System.out.println(array[i][column]);

		}
		return array;

	}

	@AfterClass
	public void tearDown() {

	}

	public void highLightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

			System.out.println(e.getMessage());
		}

		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element);

	}
}
