package x.y.z.entity.temp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TempResult2 implements Serializable {
    private int total;
    private String relatedApis;
}