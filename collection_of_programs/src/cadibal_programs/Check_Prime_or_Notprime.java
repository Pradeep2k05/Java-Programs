package cadibal_programs;
import java.util.*;
class Check_Prime_or_Notprime {

	public static void main(String[] args) {
		
		        Scanner scanner = new Scanner(System.in);
		        System.out.print("Enter a number: ");
		        int num = scanner.nextInt();
		        boolean isPrime = true;

		        if (num <= 1) {
		            isPrime = false;
		        } else {
		            for (int i = 2; i <= num / 2; i++) {
		                if (num % i == 0) {
		                    isPrime = false; 
		                    break;
		                }
		            }
		        }

		        if (isPrime)
		            System.out.println("Prime");
		        else
		            System.out.println("Not Prime");

		        scanner.close();
		    }
		

	}


