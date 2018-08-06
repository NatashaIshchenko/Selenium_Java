package tests.hotline;

import org.testng.annotations.Test;
import pages.hotline.GiftsPage;
import prototypes.BaseTest;

public class Gifts2Test extends BaseTest {
    private String projectName;

    GiftsPage giftsPage;

    public Gifts2Test(){ projectName = "hotline"; }

    public String getProjectName(){
        return projectName;
    }

    @Test(priority = 1)
    public void openGiftsPage() {
        giftsPage = new GiftsPage(getDriver(), getDomain());
    }
}
