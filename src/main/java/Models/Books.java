package Models;

import Utils.DataDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@JsonIgnoreProperties
public class Books {
        public String isbn;
        public String title;
        public String subTitle;
        public String author;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        @JsonDeserialize(using = DataDeserializer.class)
        public OffsetDateTime publish_date;
        public String publisher;
        public int pages;
        public String description;
        public String website;
}
