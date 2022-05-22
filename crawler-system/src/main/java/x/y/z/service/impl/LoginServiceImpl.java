package x.y.z.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x.y.z.entity.Login;
import x.y.z.mapper.LoginMapper;
import x.y.z.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public Login selectLoginByUserName(String userName) {
        return loginMapper.selectById(userName);
    }

    @Override
    public int updatePasswordByUserName(Login login) {
        return loginMapper.updateById(login);
    }
}
