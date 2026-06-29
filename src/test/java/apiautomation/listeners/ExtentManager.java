package apiautomation.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance()  {
   
        if (extent == null) {
        	try {
            	Path report = Paths.get(System.getProperty("user.dir"),"reports");
            	if(!Files.exists(report))
            	{
            		Files.createDirectories(report);
            	}
            	
            String reportPath = report.resolve("ExtentReport.html").toString();
            
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("Selenium Test Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("QA Name", "API Test User");
            extent.setSystemInfo("Environment", "Stage");
            extent.setSystemInfo("Platform", "NA");
        }catch(Exception e) {
        	e.getStackTrace();
        }
        }
        return extent;
    }
}