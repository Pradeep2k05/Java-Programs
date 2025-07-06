package cadibal_programs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONObject;

public class WeatherSwingApp {

    // Declare Swing components
    private JFrame frame;
    private JTextField cityTextField;
    private JButton fetchButton;
    private JLabel resultLabel;

    public static void main(String[] args) {
        // Run the GUI application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new WeatherSwingApp().createAndShowGUI());
    }

    public void createAndShowGUI() {
        // Create and set up the frame
        frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        // Create components
        cityTextField = new JTextField(20);
        fetchButton = new JButton("Get Weather");
        resultLabel = new JLabel("Enter city name to get weather.");

        // Add components to the frame
        frame.add(new JLabel("City:"));
        frame.add(cityTextField);
        frame.add(fetchButton);
        frame.add(resultLabel);

        // Action listener for the "Get Weather" button
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    @SuppressWarnings("deprecation")
	private void fetchWeather() {
        String city = cityTextField.getText().trim();

        if (city.isEmpty()) {
            resultLabel.setText("Please enter a city name.");
            return;
        }

        // Clear previous results
        resultLabel.setText("Fetching weather...");

        try {
            // API URL and Key (Replace with your own key)
            String apiKey = "64b4d4b8d5653d709bb6e39b8daf224e"; // Ensure this key is valid
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Check for errors in the response
            if (jsonResponse.has("cod") && jsonResponse.getInt("cod") != 200) {
                resultLabel.setText("Error: " + jsonResponse.getString("message"));
                return;
            }

            // Get the temperature data from the JSON response
            if (jsonResponse.has("main") && !jsonResponse.isNull("main")) {
                JSONObject main = jsonResponse.getJSONObject("main");
                double temperature = main.getDouble("temp");
                resultLabel.setText("Temperature in " + city + ": " + temperature + "°C");
            } else {
                resultLabel.setText("Error: 'main' data not found.");
            }
        } catch (Exception e) {
            resultLabel.setText("Error: " + e.getMessage());
        }
    }

	public Object createGUI() {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
