package x.y.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import x.y.z.entity.Mashups;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempResult2;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.mapper.MashupsMapper;
import x.y.z.service.MashupsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MashupsServiceImpl implements MashupsService {
    @Autowired
    private MashupsMapper mashupsMapper;
    @Cacheable(cacheNames = "mashups")
    @Override
    public List<Mashups> selectAll() {
        return mashupsMapper.selectList(null);
    }

    @Cacheable(cacheNames = "mashups")
    @Override
    public List<Mashups> selectByPage(int page,int size,String sortName) {
        Page<Mashups> pageInstance = new Page<>(page,size);
        QueryWrapper queryWrapper = null;
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper = new QueryWrapper();
            queryWrapper.orderByDesc(sortName);
        }
        Page<Mashups> mashupsPage = mashupsMapper.selectPage(pageInstance, queryWrapper);
        List records = mashupsPage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",mashupsPage.getTotal());
        records.add(map);
        return records;
    }

    @Cacheable(cacheNames = "mashups")
    @Override
    public Long getCount() {
        return mashupsMapper.selectCount(null);
    }

    @Cacheable(cacheNames = "mashups")
    @Override
    public List<TempResult> groupQuery() {
        return mashupsMapper.groupQuery();
    }

    @Cacheable(cacheNames = "mashups")
    @Override
    public List<TempResult2> selectStatisticsApis() {
        return mashupsMapper.selectStatisticsApis();
    }
    @Cacheable(cacheNames = "mashups")
    @Override
    public List<Mashups> searchLikeName(String name,String value,int page,int size,String sortName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(name!=null&&!"".equals(name)&&name.length()!=0){
            name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
            queryWrapper.like(name,value);
        }
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper.orderByDesc(sortName);
        }
        Page<Mashups> pageInstance = new Page<>(page,size);
        Page<Mashups> mashupsPage = mashupsMapper.selectPage(pageInstance,queryWrapper);
        List records = mashupsPage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",mashupsPage.getTotal());
        if(records.size()!=0){
            records.add(map);
        }else{
            records = new ArrayList();
            records.add(map);
        }
        return records;
    }
    @Cacheable(cacheNames = "mashups")
    @Override
    public List<TempTagResult> selectTag() {
        return mashupsMapper.selectTag();
    }

}
