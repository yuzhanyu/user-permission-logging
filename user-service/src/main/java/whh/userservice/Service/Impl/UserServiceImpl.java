package whh.userservice.Service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Entity.User;
import whh.userservice.Mapper.UserMapper;
import whh.userservice.Service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hanghang
 * @date 2025/6/17
 * @Description
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean registerUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(userDTO.getUsername());
        if (existingUser != null) {
            // 可以抛出自定义异常或通过其他方式处理
            return false;
        }

        // 插入新用户
        Map<String, Object> params = new HashMap<>();
        params.put("name", userDTO.getUsername());
        params.put("password", passwordEncoder.encode(userDTO.getPassword()));
        params.put("email", userDTO.getEmail());
        params.put("phone", userDTO.getPhone());
        params.put("gmtCreate", java.time.LocalDateTime.now());

        try {
            userMapper.registerUser(params);
            return true;
        } catch (Exception e) {
            // 可以记录日志或抛出异常
            return false;
        }
    }

    @Override
    public String loginUser(UserDTO userDtO) {
        User user = userMapper.findByUsername(userDtO.getUsername());
        if (user == null) {
            return null;
        }

        // 使用 BCryptPasswordEncoder 校验密码
        if (!passwordEncoder.matches(userDtO.getPassword(), user.getPassword())) {
            return null;
        }
        System.out.println(generateLoginToken());

        // 生成并返回 Token
        return generateLoginToken();
    }


    private String generateLoginToken() {
        return java.util.UUID.randomUUID().toString();
    }

}
