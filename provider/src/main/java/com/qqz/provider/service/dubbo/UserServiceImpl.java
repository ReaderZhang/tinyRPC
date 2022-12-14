package com.qqz.provider.service.dubbo;

import com.qqz.api.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        log.info("begin saveUser: "+name);
        return "Save User success";
    }
}
