package whh.userservice.Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hanghang
 * @date 2025/6/21
 * @Description
 */
//AI生成的工具类
public class IpUtils {
    /**
     * 获取客户端真实IP地址
     *
     * @param request HttpServletRequest对象
     * @return 客户端真实IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;

        // 1. 尝试从X-Forwarded-For获取（标准代理头）
        ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // 多个IP时取第一个（格式：client, proxy1, proxy2）
            return ip.split(",")[0].trim();
        }

        // 2. 尝试从Proxy-Client-IP获取（Apache代理）
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 3. 尝试从WL-Proxy-Client-IP获取（WebLogic代理）
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 4. 尝试从HTTP_CLIENT_IP获取（某些代理）
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 5. 尝试从HTTP_X_FORWARDED_FOR获取（非标准代理头）
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip.split(",")[0].trim();
        }

        // 6. 最后使用默认的远程地址
        return request.getRemoteAddr();
    }

    /**
     * 验证IP地址是否有效
     *
     * @param ip IP地址字符串
     * @return 是否有效
     */
    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }
}

