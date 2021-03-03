package net.giteye.db.service.impl;

import net.giteye.db.entity.UserInfo;
import net.giteye.db.mapper.UserInfoMapper;
import net.giteye.db.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bryan.Zhang
 * @since 2021-01-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
