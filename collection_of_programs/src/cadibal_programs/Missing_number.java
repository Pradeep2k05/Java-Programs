package cadibal_programs;
import java.util.*;
public class Missing_number {

	
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Enter n: ");
	        int n = scanner.nextInt();
	        int totalSum = n * (n + 1) / 2; 
	        int[] arr = new int[n - 1]; 
	        int arraySum = 0;
	        System.out.print("Enter a numbers: ");
	        for (int i = 0; i < n - 1; i++) {
	            arr[i] = scanner.nextInt();
	            arraySum += arr[i]; 
	        }
	        int missingNumber = totalSum - arraySum;
	        System.out.println("Missing number: " + missingNumber);

	        scanner.close();
	    }
	}


