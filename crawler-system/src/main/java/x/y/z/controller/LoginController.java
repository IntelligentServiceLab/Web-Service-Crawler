package x.y.z.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import x.y.z.entity.Login;
import x.y.z.entity.Result;
import x.y.z.service.LoginService;
import x.y.z.utils.ReturnUtil;

import java.util.UUID;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    LoginService loginService;

    /**
     * 通过密码登录时对登录密码进行校验
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/passwordLogin")
    public Result<Object> passwordLogin(String username, String password){
        Login login = loginService.selectLoginByUserName(username);
        String token = String.valueOf(UUID.randomUUID());

        if(login.getPassword().equals(password)){
            return new ReturnUtil<>().getOk(token,"密码正确，登录成功");
        }else{
            return new ReturnUtil<>().getFail("登录失败");
        }
    }

    @PostMapping("/updatePassword")
    public Result<Login> updatePassword(Login login){
        int num = loginService.updatePasswordByUserName(login);
        if(num>0){
            return new ReturnUtil<Login>().getOk(null,"更新成功");
        }else{
            return new ReturnUtil<Login>().getFail("更新失败");
        }
    }

}
