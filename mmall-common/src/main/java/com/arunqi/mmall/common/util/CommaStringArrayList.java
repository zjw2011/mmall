package com.arunqi.mmall.common.util;

import java.util.ArrayList;
import org.springframework.util.StringUtils;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/28 下午11:26
 */
public class CommaStringArrayList extends ArrayList<String> {
    public CommaStringArrayList(String commaString) {
        super(StringUtils.commaDelimitedListToSet(commaString));
    }
}
