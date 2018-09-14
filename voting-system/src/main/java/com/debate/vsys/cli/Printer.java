package com.debate.vsys.cli;

public class Printer {
	
	private static Object lock = new Object();
	
	public static void out(String str) {
		synchronized(lock) {
			System.out.print(str);
		}
	}
	
	public static void outln(String str) {
		synchronized(lock) {
			System.out.println(str);
		}
	}
	

}
