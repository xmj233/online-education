package org.hncj.orderservice.service.impl;

import org.hncj.baseservice.handle.OeException;
import org.hncj.commonutil.vo.CourseVoForOrder;
import org.hncj.commonutil.vo.UcenterMemberForOrder;
import org.hncj.orderservice.client.EduClient;
import org.hncj.orderservice.client.UcenterClient;
import org.hncj.orderservice.entity.TOrder;
import org.hncj.orderservice.mapper.TOrderMapper;
import org.hncj.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-21
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    EduClient eduClient;
    @Autowired
    UcenterClient ucenterClient;

    @Override
    public String createOrder(String courseId, String memberId) {
        CourseVoForOrder courseInfo = eduClient.getCourseInfo(courseId);
        UcenterMemberForOrder memberInfo = ucenterClient.getUcenterById(memberId);

        if(courseInfo==null) {
            throw new OeException(20001, "查询课程信息失败");
        }
        if(memberInfo==null) {
            throw new OeException(20001, "查询用户信息失败");
        }

        // 生成一个订单号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String orderNo = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            orderNo += random.nextInt(10);
        }
        orderNo = newDate + orderNo;

        TOrder order = new TOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName(courseInfo.getTeacherName());
        order.setTotalFee(courseInfo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(memberInfo.getMobile());
        order.setNickname(memberInfo.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//1：微信

        baseMapper.insert(order);

        //销量+1
        eduClient.buyCountIncrease(courseId);

        return orderNo;
    }
}
