package x.y.z.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Apis implements Serializable {
    @ExcelProperty(index = 0,value = "apiName")
    private String apiName;
    @ExcelProperty(index = 1,value = "description")
    private String description;
    @ExcelProperty(index = 2,value = "categories")
    private String categories;
    @ExcelProperty(index = 3,value = "submittedDate")
    private String submittedDate;
    @ExcelProperty(index = 4,value = "developersNumbers")
    private String developersNumbers;
    @ExcelProperty(index = 5,value = "developersName")
    private String developersName;
    @ExcelProperty(index = 6,value = "developersMR")
    private String developersMR;
    @ExcelProperty(index = 7,value = "followersNumbers")
    private String followersNumbers;
    @ExcelProperty(index = 8,value = "followersName")
    private String followersName;
    @ExcelProperty(index = 9,value = "versions")
    private String versions;
}
