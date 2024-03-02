package dev.honoreandreas.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weather-recordings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherRecord {
    @Id
    private ObjectId id;
    private String location;
    @Indexed(unique = true)
    private String date;
    private int weatherId;
    private String weatherTitle;
    private String weatherDescription;
    private String weatherIconCode;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private double windDirection;
}
