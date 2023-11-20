package org.example.utilities;

public class UiTestContext {

    private ManageWebDriver manageWebDriver;

    public UiTestContext(){
        manageWebDriver=new ManageWebDriver();
    }

    public ManageWebDriver getManageWebDriver() {
        return manageWebDriver;
    }
}
