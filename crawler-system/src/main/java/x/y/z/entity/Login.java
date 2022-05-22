package x.y.z.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
@TableName("login")
public class Login implements Serializable {
    @TableId
    private String userName;
    private String password;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date loginTime;
}
