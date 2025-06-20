package whh.userservice.Controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whh.userservice.Constant.ResultCodeConstant;
import whh.userservice.DTO.RestResult;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Entity.User;
import whh.userservice.Service.UserService;
import whh.userservice.Utils.JwtUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    @Autowired
    private JwtUtil jwtUtil;

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
    /**
     * 分页用户列表
     */
    @RequestMapping(value = "/user/users", method = RequestMethod.GET)
    public RestResult<Object> listUsers(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestHeader("Authorization") String token,
            HttpServletResponse response) { // 确保使用正确的注解和名称

        try {
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId = (Long) claims.get("userId");
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId);
            PageInfo<User> users = userService.findUsers(userDTO);
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_MSG, users);
        } catch (Exception e) {
            response.setStatus(401);
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_MSG, null);
        }
    }
    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public RestResult<Object> getUser(@RequestHeader("Authorization") String token,
                                      HttpServletResponse response,
                                      @RequestParam("userId") Long userId) {
        try {
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId1 = (Long) claims.get("userId");
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId1);
            User users = userService.getUser(userDTO, userId);
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_MSG, users);
        } catch (Exception e) {
            response.setStatus(401);
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_MSG, null);
        }
    }
    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT)
    public RestResult<Object> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDtO) {
        return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_MSG, null);
    }
    /**
     * 密码重置
     */
    @RequestMapping(value = "/user/reset-password", method = RequestMethod.POST)
    public RestResult<Object> resetPassword(@PathVariable Long userId, @RequestBody UserDTO userDtO) {
        return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_MSG, null);
    }



}
