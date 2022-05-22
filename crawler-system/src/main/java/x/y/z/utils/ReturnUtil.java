package x.y.z.utils;

import x.y.z.entity.ConstValue;
import x.y.z.entity.Result;

public class ReturnUtil<T> {

    /**
     * 获取数据成功，同时返回自己传的data和msg
     * @param data 数据
     * @param msg 提示信息
     * @return 结果
     */
    public Result<T> getOk(T data, String msg){
        return new Result<>(ConstValue.SUCCESS_CODE, data, msg);
    }

    /**
     * 获取数据成功，只传data，msg都是成功
     * @param data 数据
     * @return 结果
     */
    public Result<T> getOk(T data){
        return new Result<>(ConstValue.SUCCESS_CODE, data, ConstValue.SUCCESS_MSG);
    }

    /**
     * 获取数据失败，同时返回自己传的msg
     * @param msg 提示信息
     * @return 结果
     */
    public Result<T> getFail(String msg){
        return new Result<>(ConstValue.FAIL_CODE, null, msg);
    }

    /**
     * 获取数据失败
     * @return 结果
     */
    public Result<T> getFail(){
        return new Result<>(ConstValue.FAIL_CODE, null, ConstValue.FAIL_MSG);
    }


}