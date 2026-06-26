package apiautomation.listeners;


import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;



public class TestListener implements ITestListener {

	ExtentReports extent = ExtentManager.getInstance();
	//ExtentTest test; // Non local Thread Implementation
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		// test = extent.createTest(result.getMethod().getMethodName());
		ExtentTest test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentTest.get().pass("Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
	
		extentTest.get().fail(result.getThrowable());

	}

	@Override
	public void onTestSkipped(ITestResult result) {

		extentTest.get().skip("Test Skipped");
	}

	@Override
	public void onFinish(org.testng.ITestContext context) {
		extent.flush();
	}
}