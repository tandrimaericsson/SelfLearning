package org.example.stepDefinitions;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.example.utilities.ManageWebDriver;
import org.example.utilities.UiTestContext;

public class hooks {

    private ManageWebDriver manageWebDriver;



    @Before(order=2)
    public void before2(){
        System.out.println("Before method for all 2");
    }

    @Before("@TC_007")
    public void before07(){
        System.out.println("Before method for 007");
    }

    @Before()
    public void before3(){
        System.out.println("Before method for all 3");
    }


}
