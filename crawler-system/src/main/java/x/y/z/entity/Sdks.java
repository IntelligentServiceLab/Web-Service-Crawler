package x.y.z.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Sdks implements Serializable {
    @ExcelProperty(index = 0,value = "sdksName")
    private String sdksName;
    @ExcelProperty(index = 1,value = "description")
    private String description;
    @ExcelProperty(index = 2,value = "relatedApis")
    private String relatedApis;
    @ExcelProperty(index = 3,value = "relatedPlatformLanguages")
    private String relatedPlatformLanguages;
    @ExcelProperty(index = 4,value = "categories")
    private String categories;
    @ExcelProperty(index = 5,value = "addedDate")
    private String addedDate;
    @ExcelProperty(index = 6,value = "sdkProvider")
    private String sdkProvider;
    @ExcelProperty(index = 7,value = "followersNumber")
    private String followersNumber;
    @ExcelProperty(index =8,value = "followers")
    private String followers;

}
