package tests.rozetka;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.rozetka.SmartphonePage;
import prototypes.BaseTest;

public class SmartphoneTest extends BaseTest {

    private String projectName;

    SmartphonePage smartphonePage;

    public SmartphoneTest() {
        projectName = "rozetka";
    }

    public String getProjectName() {
        return projectName;
    }

    @DataProvider(name = "Labels1")
    public static Object[][] getLabels1() {
        return new Object[][]{
                {"action"},
                {"super_price"}};
    }

    @DataProvider(name = "Labels2")
    public static Object[][] getLabels2() {
        return new Object[][]{
                {"popular"}};
    }

    @Test(priority = 1)
    public void openPage() {
        smartphonePage = new SmartphonePage(getDriver(), getDomain());
    }

    @Test(dataProvider = "Labels1", priority = 2, testName = "getPhoneByLabel_1")
    public void getPhoneByLabel_1(String label) {
        System.out.println("Label1 ========= " + label + " ============");
        smartphonePage.getPhoneName(label);
        Assert.assertEquals(1,2);
    }

    @Test(dataProvider = "Labels2", priority = 2, testName = "getPhoneByLabel_1")
    public void getPhoneByLabel_2(String label) {
        System.out.println("Label2 ========= " + label + " ============");
        smartphonePage.getPhoneName(label);
    }
}