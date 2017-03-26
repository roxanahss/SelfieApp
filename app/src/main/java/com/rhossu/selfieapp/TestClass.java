package com.rhossu.selfieapp;

/**
 * Created by Roxanah on 3/26/2017.
 */
public class TestClass {
    private static TestClass ourInstance = new TestClass();

    public static TestClass getInstance() {
        return ourInstance;
    }

    private TestClass() {
    }
}
