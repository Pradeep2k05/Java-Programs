package javaname;

import json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Scanner;

public class Weather {

    @SuppressWarnings("deprecation")
	public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter city name (or type 'exit' to quit): ");
            String city = scanner.nextLine().trim();

            if (city.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                String apiKey = "64b4d4b8d5653d709bb6e39b8daf224e"; // Ensure this key is valid
                // Use the correct URL for city name
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Print the raw response for debugging
                System.out.println("Raw response: " + response.toString());

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Check if there's an error in the response
                if (jsonResponse.has("cod") && jsonResponse.getInt("cod") != 200) {
                    System.out.println("Error: " + jsonResponse.getString("message"));
                    continue;  // Skip this iteration and prompt the user again
                }

                // Check for the 'main' data
                if (jsonResponse.has("main") && !jsonResponse.isNull("main")) {
                    JSONObject main = jsonResponse.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    System.out.println("The temperature in " + city + " is: " + temperature + "°C");
                } else {
                    System.out.println("Error: 'main' data not found in the response.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}