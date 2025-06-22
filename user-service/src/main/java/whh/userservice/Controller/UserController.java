package whh.userservice.Controller;

import com.github.pagehelper.PageInfo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import whh.userservice.Constant.ResultCodeConstant;
import whh.userservice.DTO.RestResult;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Entity.Log;
import whh.userservice.Entity.User;
import whh.userservice.Service.UserService;
import whh.userservice.Utils.IpUtils;
import whh.userservice.Utils.JwtUtil;
import whh.userservice.Utils.LogUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */
@RestController
public class UserController {
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 用户注册
     *
     * @param userDtO 用户注册信息
     * @return
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public RestResult<Boolean> registerUser(@RequestBody UserDTO userDtO) {
        Boolean result = userService.registerUser(userDtO);
        if(result==true){
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_REGISTER, result);
        }else{
            return new RestResult<>(ResultCodeConstant.FAIL,ResultCodeConstant.FAIL_USER_REGISTER,result);

        }
    }

    /**
     * 用户登录
     *
     * @param userDtO 用户登录信息
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public RestResult<String> login(@RequestBody UserDTO userDtO,
                                    HttpServletRequest request) {
        String token = userService.loginUser(userDtO);
        if(token==null){
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_USER_LOGIN, null);
        }else{
            logUtils.recordLoginSuccess(token);
//            Map<String, Object> claims = jwtUtil.parseToken(token);
//            Long userId = (Long) claims.get("userId");
//            String IpClient= IpUtils.getClientIp(request);
//            Log log=new Log();
//            log.setUserId(userId);
//            log.setAction("登录操作");
//            log.setIp(IpClient);
//            log.setDetail("用户"+userId+"在"+IpClient+"登录成功");
//            rocketMQTemplate.send("log-topic", MessageBuilder.withPayload(log).build());
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_LOGIN, token);
        }
    }
    /**
     * 分页用户列表
     */
    @RequestMapping(value = "/user/users", method = RequestMethod.GET)
    public RestResult<Object> listUsers(
            @RequestHeader("Authorization") String token,
            HttpServletResponse response) {

        try {
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId = (Long) claims.get("userId");
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId);
            PageInfo<User> users = userService.findUsers(userDTO);
            logUtils.recordUserAction(
                    token,
                    "分页用户列表",
                    "用户查看自己权限内所以的用户信息"
            );
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_GET_USER, users);
        } catch (Exception e) {
            response.setStatus(401);
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_USER_GET, null);
        }
    }
    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public RestResult<Object> getUser(@RequestHeader("Authorization") String token,
                                      HttpServletResponse response,
                                      @PathVariable("userId") Long userId) {
        try {
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId1 = (Long) claims.get("userId");
            User users = userService.getUser(userId1, userId);
            logUtils.recordUserAction(
                    token,
                    "查询用户信息",
                    "用户查询了权限内的用户信息"
            );
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_GET_USER, users);
        } catch (Exception e) {
            response.setStatus(401);
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_USER_GET, null);
        }
    }
    /**
     * 修改用户信息
     */
    @Transactional
    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    public RestResult<Object> updateUser(
            @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token,
                                         HttpServletResponse response) {
        try {
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId1 = (Long) claims.get("userId");
            userService.updateUser(userId1,userDTO);
            logUtils.recordUserAction(
                    token,
                    "修改用户信息",
                    "修改自己权限内的用户信息"
            );
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_UPDATE_USER, true);
        } catch (Exception e) {
            response.setStatus(401);
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_USER_UPDATE, false);
        }
    }
    /**
     * 密码重置
     */
    @RequestMapping(value = "/user/reset-password", method = RequestMethod.POST)
    public RestResult<Object> resetPassword(
            @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token,
            HttpServletResponse response) {
        try {
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId1 = (Long) claims.get("userId");
            userService.resetPassword(userId1,userDTO);
            logUtils.recordUserAction(
                    token,
                    "重置密码",
                    "用户重置了自己权限内的用户密码"
            );
            return new RestResult<>(ResultCodeConstant.SUCCESS, ResultCodeConstant.SUCCESS_RESET_PASSWORD, true);
        } catch (Exception e) {
            response.setStatus(401);
            return new RestResult<>(ResultCodeConstant.FAIL, ResultCodeConstant.FAIL_USER_RESET_PASSWORD, false);
        }
    }




}
