package x.y.z.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName(value = "mashups")
@Data
@NoArgsConstructor
public class Mashups implements Serializable {
    @ExcelProperty(index = 0,value = "mashupsName")
    private String mashupsName;
    @ExcelProperty(index = 1,value = "relatedApis")
    private String relatedApis;
    @ExcelProperty(index = 2,value = "description")
    private String description;
    @ExcelProperty(index = 3,value = "categories")
    private String categories;
    @ExcelProperty(index = 4,value = "submittedDate")
    private String submittedDate;
    @ExcelProperty(index = 5,value = "mashupAppType")
    private String mashupAppType;
    @ExcelProperty(index = 6,value = "company")
    private String company;
    @ExcelProperty(index = 7,value = "followersNumbers")
    private String followersNumbers;
    @ExcelProperty(index = 8,value = "followersNames")
    private String followersNames;

}
