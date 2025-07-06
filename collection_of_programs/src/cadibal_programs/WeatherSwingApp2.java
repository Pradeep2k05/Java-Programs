package cadibal_programs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import org.json.*;

public class WeatherSwingApp2 {
    private JFrame frame;
    private JTextField cityField;
    private JButton fetchButton;
    private JLabel resultLabel;
    private JPanel weatherPanel;
    private JLabel temperatureLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;
    private JLabel descriptionLabel;
    private JLabel cityCountryLabel;

    // Replace with your OpenWeatherMap API key
    private static final String API_KEY = "64b4d4b8d5653d709bb6e39b8daf224e";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherSwingApp().createGUI());
    }

    public void createGUI() {
        // Create main frame
        frame = new JFrame("Weather Information App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(new Color(135, 206, 250)); // Sky blue
        
        JLabel cityLabel = new JLabel("Enter City:");
        cityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        cityField = new JTextField(15);
        cityField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        fetchButton = new JButton("Get Weather");
        fetchButton.setFont(new Font("Arial", Font.BOLD, 14));
        fetchButton.setBackground(new Color(30, 144, 255)); // Dodger blue
        fetchButton.setForeground(Color.WHITE);
        
        searchPanel.add(cityLabel);
        searchPanel.add(cityField);
        searchPanel.add(fetchButton);
        
        // Create result panel
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        weatherPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        
        cityCountryLabel = new JLabel("Weather Information");
        cityCountryLabel.setFont(new Font("Arial", Font.BOLD, 24));
        cityCountryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        resultLabel = new JLabel("Enter a city name above and click 'Get Weather'");
        resultLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        temperatureLabel = new JLabel("");
        temperatureLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        descriptionLabel = new JLabel("");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        humidityLabel = new JLabel("");
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        windLabel = new JLabel("");
        windLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        windLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        weatherPanel.add(Box.createVerticalStrut(20));
        weatherPanel.add(cityCountryLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(resultLabel);
        weatherPanel.add(Box.createVerticalStrut(20));
        weatherPanel.add(temperatureLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(descriptionLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(humidityLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(windLabel);
        
        // Add panels to frame
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(weatherPanel, BorderLayout.CENTER);
        
        // Center frame on screen
        frame.setLocationRelativeTo(null);
        
        // Add action listeners
        fetchButton.addActionListener(e -> fetchWeather());
        cityField.addActionListener(e -> fetchWeather());
        
        // Display the frame
        frame.setVisible(true);
    }

    private void fetchWeather() {
        String city = cityField.getText().trim();

        if (city.isEmpty()) {
            showError("Please enter a city name!");
            return;
        }

        resultLabel.setText("Loading weather information...");
        clearWeatherInfo();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(city, "UTF-8")
                            + "&appid=" + "64b4d4b8d5653d709bb6e39b8daf224e"
                            + "&units=metric";

                    // Make API call
                    String jsonResponse = makeAPICall(url);

                    // Parse response
                    JSONObject data = new JSONObject(jsonResponse);

                    // Check for API errors
                    if (data.has("cod") && !data.get("cod").toString().equals("200")) {
                        String errorMsg = data.optString("message", "Unknown API error");
                        throw new Exception("API Error: " + errorMsg);
                    }

                    // Extract weather data
                    String cityName = data.getString("name");
                    String countryCode = data.getJSONObject("sys").getString("country");
                    
                    JSONObject main = data.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    double feelsLike = main.getDouble("feels_like");
                    int humidity = main.getInt("humidity");
                    
                    JSONObject weatherData = data.getJSONArray("weather").getJSONObject(0);
                    String weatherMain = weatherData.getString("main");
                    String weatherDesc = weatherData.getString("description");
                    
                    double windSpeed = data.getJSONObject("wind").getDouble("speed");

                    // Update UI
                    SwingUtilities.invokeLater(() -> {
                        updateWeatherInfo(cityName, countryCode, temp, feelsLike, humidity, weatherMain, weatherDesc, windSpeed);
                    });

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        showError("Error: " + e.getMessage());
                    });
                }
                return null;
            }
        }.execute();
    }

    private void updateWeatherInfo(String cityName, String country, double temp, double feelsLike, 
                                 int humidity, String weatherMain, String weatherDesc, double windSpeed) {
        cityCountryLabel.setText(cityName + ", " + country);
        resultLabel.setText(weatherMain);
        temperatureLabel.setText(String.format("Temperature: %.1f°C (Feels like: %.1f°C)", temp, feelsLike));
        descriptionLabel.setText("Weather: " + weatherDesc);
        humidityLabel.setText("Humidity: " + humidity + "%");
        windLabel.setText("Wind: " + windSpeed + " m/s");
    }

    private void clearWeatherInfo() {
        temperatureLabel.setText("");
        descriptionLabel.setText("");
        humidityLabel.setText("");
        windLabel.setText("");
    }

    private void showError(String message) {
        resultLabel.setText(message);
        clearWeatherInfo();
    }

    private String makeAPICall(String urlString) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                throw new IOException("Server returned HTTP " + responseCode + ": " + response.toString());
            }

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();

        } finally {
            if (reader != null) reader.close();
            if (conn != null) conn.disconnect();
        }
    }
}