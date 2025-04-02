package cadibal_programs;

import java.util.*;
public class Sum_of_Digits {

	public static void main(String[] args) {
	
	Scanner s=new Scanner(System.in);
	
	
	        System.out.print("Enter a number: ");
	        int num =s.nextInt();
	       int sum = 0;

	        for (sum=0; num > 0; num /= 10) {
	        	sum+=num%10;
	        }

	        System.out.println("Sum of digits: " + sum);
	        s.close();
	    }
	

	}


