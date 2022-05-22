package x.y.z.service;

import x.y.z.entity.Login;
public interface LoginService {
    /**
     * 通过用户名获取记录
     * @param userName
     * @return
     */
    Login selectLoginByUserName(String userName);

    /**
     * 更新密码
     * @param login
     * @return
     */
    int updatePasswordByUserName(Login login);


}
