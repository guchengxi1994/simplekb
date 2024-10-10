package org.xiaoshuyui.simplekb;

public class MyClass {
    private String status;

    public MyClass(String status) {
        this.status = status;
    }

    public void methodA() {
        System.out.println("Executing Method A");
    }

    public void methodB() {
        System.out.println("Executing Method B");
    }

    public void methodDefault() {
        System.out.println("Executing Default Method");
    }

    // getter and setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
