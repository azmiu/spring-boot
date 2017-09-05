package com.azmiu.test;


public interface Aircondition {

}

class AirconditionA implements Aircondition {

    public AirconditionA() {
        System.out.println("制造-->AirconditionA");
    }
}

class AirconditionB implements Aircondition {

    public AirconditionB() {
        System.out.println("制造-->AirconditionB");
    }
}
