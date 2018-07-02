package com.arunqi.mmall.server.leaf.service;

import com.arunqi.mmall.facade.leaf.service.LeafService;
import com.arunqi.mmall.server.leaf.repository.LeafRepository;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 对外提供服务接口.
 *
 * @author jiawei zhang
 * @datetime 2018/7/1 上午11:00
 */
@Service("leafService")
public class LeafServiceImpl implements LeafService {

    @Resource
    private LeafRepository leafRepository;

    @Override
    public Long getId(String bizName) {
        return leafRepository.getId(bizName);
    }
}
