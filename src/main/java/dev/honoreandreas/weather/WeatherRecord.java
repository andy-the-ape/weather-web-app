package dev.honoreandreas.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weather-recordings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherRecord {
    @Id
    private ObjectId id;
    private String title;
    private String date;
    private String description;
    private int temperature;
    private int humidity;
    private String weatherPicture;
}
