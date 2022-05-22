package x.y.z.entity.temp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class TempTagResult implements Serializable {
    private int total;
    private String tag2;
}
