package whh.userservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whh.userservice.Constant.ResultCodeConstant;
import whh.userservice.DTO.RestResult;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Service.UserService;

/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
//@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userDtO 用户注册信息
     * @return
     */

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public RestResult<Boolean> registerUser(@RequestBody UserDTO userDtO) {
        Boolean result = userService.registerUser(userDtO);
        return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_MSG, result);
    }
    /**
     * 用户登录
     *
     * @param userDtO 用户登录信息
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public RestResult<String> login(@RequestBody UserDTO userDtO) {
        String token = userService.loginUser(userDtO);
        if(token==null){
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_MSG, null);
        }else{
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_MSG, token);
        }

    }

}
