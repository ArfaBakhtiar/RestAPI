package gpApiTests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class BaseTest {
	private static ExtentHtmlReporter htmlReporter;
    private static ExtentReports extent;
    private ThreadLocal<ExtentTest> parentNode = new ThreadLocal<ExtentTest>();
    private ThreadLocal<ExtentTest> extenttest = new ThreadLocal<ExtentTest>();
    public ExtentTest test;
    
    
    @BeforeSuite
    public void setttingUpReportConfig() throws IOException {
        Date date= new Date();
        String d=date.toString().replaceAll(":", "-");
        Reporter.log("Date:"+date,true);
        
        Reporter.log("STarted",true);
        
        File file=new File(".\\ogs-test-reports\\ogsTestreport"+d+".html");
        Reporter.log("Directory path==="+file.getAbsolutePath(),true);
        Reporter.log("file created...."+file.createNewFile(),true);
        htmlReporter= new ExtentHtmlReporter(file);
        extent= new ExtentReports();
        extent.attachReporter(htmlReporter);

        extent.setSystemInfo("Opereating System", "Windows10");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Host Name", "Durga Verma");
        extent.setSystemInfo("User", "Durga Verma");

        htmlReporter.config().setDocumentTitle("OGSTestReport");
        htmlReporter.config().setReportName("OGSTestReport");
        htmlReporter.config().setTheme(Theme.STANDARD);
    }
    
    @BeforeClass
    public synchronized void creatingParentNode() {
        ExtentTest parent = extent.createTest(getClass().getName());
        parentNode.set(parent);
    }

    @BeforeMethod
    public synchronized void creatingChildNode(Method method) {
        test = parentNode.get().createNode(method.getName());
        extenttest.set(test);
    }
    
    @AfterMethod
    public synchronized void GettingTestResult(ITestResult iTestResult) {
    	if (iTestResult.getStatus() == ITestResult.FAILURE)
        {
            extenttest.get().fail(iTestResult.getThrowable());
        }

        else if (iTestResult.getStatus() == ITestResult.SKIP)
        {
        	extenttest.get().skip(iTestResult.getThrowable());
        }
            
        else
            extenttest.get().pass("Test passed");
        
    }
    
    @AfterSuite
    public void generatingReport()
    {
        extent.flush();
    }
    
}
