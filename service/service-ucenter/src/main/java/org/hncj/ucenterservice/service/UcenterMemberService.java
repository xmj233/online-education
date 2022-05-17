package org.hncj.ucenterservice.service;

import org.hncj.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hncj.ucenterservice.entity.vo.LoginVo;
import org.hncj.ucenterservice.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-17
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    Integer countRegister(String day);

    Integer test();
}
