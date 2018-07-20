package com.arunqi.mmall.server.leaf;

import org.springframework.util.AntPathMatcher;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/7/3 下午5:02
 */
public class TestUtil {
    public static void main(String[] args) {
        final AntPathMatcher pathMatcher = new AntPathMatcher();
        pathMatcher.setCaseSensitive(false);
        System.out.println(pathMatcher.match("Test*".replace('.', '/'), "test"));
    }
}
