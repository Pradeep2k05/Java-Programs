package cadibal_programs;
import java.util.Scanner;

public class Chick_Palindrome_Or_Not {		
	    public static void main(String[] args) {
	        Scanner s = new Scanner(System.in);	        	        
	        System.out.print("Enter a string: ");
	        String input = s.nextLine(); 	        
	        String name = input.replaceAll("\\s", "").toLowerCase();
	        String reversedString = new StringBuilder(name).reverse().toString();
	        if (name.equals(reversedString)) {
	            System.out.println("Palindrome");
	        } else {
	            System.out.println("Not a palindrome");
	            		
	            		s.close();
	        }
	    }
	}


