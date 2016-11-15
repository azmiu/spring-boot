package com.azmiu;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		ArrayList abc = new ArrayList<>();
		String a = "b";
		for(int i = 0;i < 1000; i++){
			a += i;
		}
		System.out.println(a);
	}

}
