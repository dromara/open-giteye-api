package net.giteye.db.service.impl;

import net.giteye.db.entity.ClientLogin;
import net.giteye.db.mapper.ClientLoginMapper;
import net.giteye.db.service.ClientLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bryan.Zhang
 * @since 2021-01-27
 */
@Service
public class ClientLoginServiceImpl extends ServiceImpl<ClientLoginMapper, ClientLogin> implements ClientLoginService {

}
