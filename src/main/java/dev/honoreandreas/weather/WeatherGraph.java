package dev.honoreandreas.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "weather_graphs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherGraph {
    @Id
    private ObjectId id;
    private String startDate;
    private String endDate;
    @DocumentReference
    private List<WeatherRecord> weatherRecordIds;
}
