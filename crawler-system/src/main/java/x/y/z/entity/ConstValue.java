package x.y.z.entity;

import java.io.Serializable;

public class ConstValue implements Serializable {
    /** 请求成功的返回码 */
    public final static Integer SUCCESS_CODE = 200;

    /** 请求失败的返回码 */
    public final static Integer FAIL_CODE = 400;

    /** 获取成功信息 */
    public final static String SUCCESS_MSG = "获取成功";

    /** 获取失败信息 */
    public final static String FAIL_MSG = "获取失败";

    /**验证失败*/
    public static final String AUTH_FA_IL = "获取数据失败";
}
