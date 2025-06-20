package whh.userservice.Service.Impl;

import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whh.userservice.Client.UserClient;
import whh.userservice.DTO.UserDTO;
import whh.userservice.Entity.User;
import whh.userservice.Exception.PermissionServiceException;
import whh.userservice.Mapper.UserMapper;
import whh.userservice.Service.UserService;
import whh.userservice.Utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserClient userClient;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserClient userClient, UserMapper userMapper, PasswordEncoder passwordEncoder ,JwtUtil jwtUtil) {
        this.userClient = userClient;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Boolean registerUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(userDTO.getUsername());
        if (existingUser != null) {
            log.warn("用户名已存在: {}", userDTO.getUsername());
            return false;
        }
        // 插入新用户
        Map<String, Object> params = new HashMap<>();
        params.put("username", userDTO.getUsername());
        params.put("password", passwordEncoder.encode(userDTO.getPassword()));
        params.put("email", userDTO.getEmail());
        params.put("phone", userDTO.getPhone());
        params.put("gmtCreate", LocalDateTime.now());
        try {
            userMapper.registerUser(params);
            // 获取新注册用户的ID (假设Mapper返回或设置ID)
            Long newUserId = (Long) params.get("id");
            // 如果Mapper没有返回ID，需要通过用户名查询
            if (newUserId == null) {
                User newUser = userMapper.findByUsername(userDTO.getUsername());
                newUserId = newUser.getUserId();
            }
            log.info("新用户注册成功: {}, ID: {}", userDTO.getUsername(), newUserId);
            // 调用权限服务绑定默认角色
            bindDefaultRoleForUser(newUserId);
            return true;
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage(), e);
            return false;
        }
    }
    /**
     * 为用户绑定默认角色
     * @param userId 用户ID
     */
    private void bindDefaultRoleForUser(Long userId) {
        try {
            log.debug("为用户 {} 绑定默认角色", userId);
            userClient.bindDefaultRole(userId);
            log.info("用户 {} 默认角色绑定成功", userId);

            // 验证角色绑定
            String roleCode = userClient.getUserRoleCode(userId);
            log.info("用户 {} 当前角色: {}", userId, roleCode);

        } catch (Exception e) {
            log.error("绑定默认角色失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new PermissionServiceException("角色绑定服务不可用");
        }
    }

    @Override
    public String loginUser(UserDTO userDTO) {
        User user = userMapper.findByUsername(userDTO.getUsername());
        if (user == null) {
            log.warn("登录失败: 用户名不存在 - {}", userDTO.getUsername());
            return null;
        }
        // 密码验证
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            log.warn("登录失败: 密码错误 - 用户: {}", userDTO.getUsername());
            return null;
        }
        // 获取用户角色
        try {
            String roleCode = userClient.getUserRoleCode(user.getUserId());
            log.info("用户 {} 登录成功，角色: {}", userDTO.getUsername(), roleCode);

            // 基于角色生成不同权限的token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("username", userDTO.getUsername());
            claims.put("role", roleCode);
            return jwtUtil.genToken(claims);
        } catch (Exception e) {
            log.error("获取用户角色失败: {}", e.getMessage());
            return "";
        }
    }
    /**
     * 分页用户列表
     */
    @Override
    public PageInfo<User> findUsers(UserDTO userDTO) {

        //  获取用户信息
        User currentUser = userMapper.findById(userDTO.getUserId());
        if (currentUser == null) {
            log.warn("用户不存在: {}", currentUser);
        }
        log.info("当前用户: {}", currentUser);

        // 获取当前用户角色
        String roleCode = userClient.getUserRoleCode(userDTO.getUserId());
        // 设置分页参数（前端传入）
        int pageNum = userDTO.getPageNum() != null ? userDTO.getPageNum() : 1;
        int pageSize = userDTO.getPageSize() != null ? userDTO.getPageSize() : 10;

        // 根据角色构建不同查询条件
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList=Collections.emptyList();

        if ("SUPER_ADMIN".equals(roleCode)) {
            // 超级管理员：查看所有用户
            userList = userMapper.findAllUsers();
        } else if ("ADMIN".equals(roleCode)) {
            List<Long> orgUserIds = List.of(userClient.getUserIdsByRoleCode("USER"));
            log.info("获取组织用户ID列表: {}", orgUserIds);

            // 创建查询条件
            UserDTO queryDTO = new UserDTO();
            for(int i=0;i<orgUserIds.size();i++){
                queryDTO.setUserId(orgUserIds.get(i));
                System.out.println(i);
            }

            userList = userMapper.findUsers(queryDTO);

        } else if ("USER".equals(roleCode)) {
            // 普通用户：只能查看自己
            userDTO.setUserId(Long.valueOf(currentUser.getUserId()));
            User user= userMapper.findById(userDTO.getUserId());
            userList = Collections.singletonList(user);
        }

        // 包装分页结果
        return new PageInfo<>(userList);
    }

    @Override
    public User getUser(UserDTO userDTO,Long userId) {
        //  获取用户信息
        User currentUser = userMapper.findById(userDTO.getUserId());
        if (currentUser == null) {
            log.warn("用户不存在: {}", currentUser);
        }
        log.info("当前用户: {}", currentUser);
        // 获取当前用户角色
        String roleCode = userClient.getUserRoleCode(userDTO.getUserId());
        User user= null;
        if ("SUPER_ADMIN".equals(roleCode)) {
            // 超级管理员：查看所有用户
            user = userMapper.findById(userId);
        } else if ("ADMIN".equals(roleCode)) {
            List<Long> orgUserIds = List.of(userClient.getUserIdsByRoleCode("USER"));
            log.info("获取组织用户ID列表: {}", orgUserIds);
            for(int i=0;i<orgUserIds.size();i++){
                if(orgUserIds.get(i)==userId){
                    user = userMapper.findById(userId);
                    break;
                }
            }
            System.out.println(user);
        } else if ("USER".equals(roleCode)) {
            if(userId==currentUser.getUserId()){
                user = userMapper.findById(userId);
            }
        }
        return user;
    }


    @Override
    public Boolean updateUser(Long userId, UserDTO userDtO) {
        return null;
    }

    @Override
    public Boolean resetPassword(Long userId, UserDTO userDtO) {
        return null;
    }

}
