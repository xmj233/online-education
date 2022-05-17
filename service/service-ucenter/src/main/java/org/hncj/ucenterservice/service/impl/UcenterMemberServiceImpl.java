package org.hncj.ucenterservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hncj.baseservice.handle.OeException;
import org.hncj.commonutil.JwtUtils;
import org.hncj.commonutil.MD5;
import org.hncj.ucenterservice.entity.UcenterMember;
import org.hncj.ucenterservice.entity.vo.LoginVo;
import org.hncj.ucenterservice.entity.vo.RegisterVo;
import org.hncj.ucenterservice.mapper.UcenterMemberMapper;
import org.hncj.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-17
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            throw new OeException(20001, "注册信息不完整");
        }

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0) {
            throw new OeException(20001, "此手机号已经注册过");
        }

        Object o = redisTemplate.opsForValue().get(mobile);
        if ( o==null ) {
            throw new OeException(20001, "验证码不正确");
        }
        String redisCode = o.toString();
        if(!redisCode.equals(code)) {
            throw new OeException(20001, "验证码不正确");
        }

        String md5Password = MD5.encrypt(password);
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setNickname(nickname);
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(md5Password);
        ucenterMember.setAvatar("https://online-education202204.oss-cn-beijing.aliyuncs.com/default_img/defaultIcon.jpg");
        ucenterMember.setIsDisabled(false);
        baseMapper.insert(ucenterMember);
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new OeException(20001,"手机号或密码有误");
        }

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if ( ucenterMember==null ) {
            throw new OeException(20001, "手机号或密码有误");
        }

        String md5Password = MD5.encrypt(password);
        if ( !md5Password.equals(ucenterMember.getPassword())) {
            throw new OeException(20001, "用户名和密码有误");
        }

        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

        return jwtToken;
    }

    @Override
    public Integer countRegister(String day) {
        Integer count = baseMapper.countRegister(day);
        return count;
    }

    @Override
    public Integer test() {
        return baseMapper.test();
    }
}
