package net.giteye.db.service.impl;

import net.giteye.db.entity.GiteeUserAuth;
import net.giteye.db.mapper.GiteeUserAuthMapper;
import net.giteye.db.service.GiteeUserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bryan.Zhang
 * @since 2021-01-25
 */
@Service
public class GiteeUserAuthServiceImpl extends ServiceImpl<GiteeUserAuthMapper, GiteeUserAuth> implements GiteeUserAuthService {

}
