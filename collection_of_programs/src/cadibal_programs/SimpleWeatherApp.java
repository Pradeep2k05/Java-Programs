package cadibal_programs;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class SimpleWeatherApp {
    private JFrame frame;
    private JTextField cityTextField;
    private JButton getWeatherButton;
    private JLabel resultLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleWeatherApp().createGUI());
    }

    public void createGUI() {
        frame = new JFrame("Simple Weather Checker");
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cityTextField = new JTextField(20);
        getWeatherButton = new JButton("Check Weather");
        resultLabel = new JLabel("Enter city and press button.");

        frame.add(new JLabel("City:"));
        frame.add(cityTextField);
        frame.add(getWeatherButton);
        frame.add(resultLabel);

        getWeatherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = cityTextField.getText().trim();
                if (!city.isEmpty()) {
                    fetchWeather(city);
                } else {
                    resultLabel.setText("City name required!");
                }
            }
        });

        frame.setVisible(true);
    }

    private void fetchWeather(String city) {
        try {
            String apiKey = "3db8c58b43ddec5a64cd916ff8be7c7c"; // Replace with your OpenWeatherMap API key
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject weatherData = new JSONObject(response.toString());
            double temp = weatherData.getJSONObject("main").getDouble("temp");

            resultLabel.setText("Temperature in " + city + ": " + temp + "°C");
        } catch (Exception e) {
            resultLabel.setText("Error: " + e.getMessage());
        }
    }
}