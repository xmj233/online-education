package org.hncj.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hncj.eduservice.entity.EduTeacher;
import org.hncj.eduservice.mapper.EduTeacherMapper;
import org.hncj.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author xumgjc
 * @since 2022-04-11
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public Map<String, Object> getTeacherList(Page<EduTeacher> page) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        baseMapper.selectPage(page, wrapper);

        // 当前页的数据
        List<EduTeacher> records = page.getRecords();
        // 当前页
        long current = page.getCurrent();
        // 多少页
        long pages = page.getPages();
        // 每页大小
        long size = page.getSize();
        // 数据总数
        long total = page.getTotal();
        // 是否有下一页
        boolean hasNext = page.hasNext();
        // 是否有上一页
        boolean hasPrevious = page.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("records", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }
}
