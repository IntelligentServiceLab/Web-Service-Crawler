package x.y.z.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SampleSourceCode implements Serializable {
    @ExcelProperty(index = 0,value = "sampleSourceCodeName")
    private String sampleSourceCodeName;
    @ExcelProperty(index = 2,value = "description")
    private String description;
    @ExcelProperty(index = 3,value = "relatedApis")
    private String relatedApis;
    @ExcelProperty(index = 4,value = "relatedPlatformLanguages")
    private String relatedPlatformLanguages;
    @ExcelProperty(index = 5,value = "categories")
    private String categories;
    @ExcelProperty(index = 6,value = "addedDate")
    private String addedDate;
    @ExcelProperty(index = 7,value = "versions")
    private String versions;
    @ExcelProperty(index = 8,value = "sampleSourceCodeProvider")
    private String sampleSourceCodeProvider;
    @ExcelProperty(index = 9,value = "followersNumber")
    private String followersNumber;
    @ExcelProperty(index = 10,value = "followers")
    private String followers;
}
