package x.y.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import x.y.z.entity.Sdks;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempResult2;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.mapper.SdksMapper;
import x.y.z.service.SdksService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SdksServiceImpl implements SdksService {
    @Autowired
    private SdksMapper sdksMapper;
    @Cacheable(cacheNames = "sdks")
    @Override
    public List<Sdks> selectAll() {
        return sdksMapper.selectList(null);
    }

    @Cacheable(cacheNames = "sdks")
    @Override
    public List<Sdks> selectByPage(int page,int size,String sortName) {
        Page<Sdks> pageInstance = new Page<>(page,size);
        QueryWrapper queryWrapper = null;
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper = new QueryWrapper();
            queryWrapper.orderByDesc(sortName);
        }
        Page<Sdks> sdksPage = sdksMapper.selectPage(pageInstance, null);
        List records = sdksPage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",sdksPage.getTotal());
        records.add(map);
        return records;
    }

    @Cacheable(cacheNames = "sdks")
    @Override
    public Long getCount() {
        return sdksMapper.selectCount(null);
    }

    @Cacheable(cacheNames = "sdks")
    @Override
    public List<TempResult> groupQuery() {
        return sdksMapper.groupQuery();
    }
    @Cacheable(cacheNames = "sdks")
    @Override
    public List<TempResult2> selectStatisticsApis() {
        return sdksMapper.selectStatisticsApis();
    }
    @Cacheable(cacheNames = "sdks")
    @Override
    public List<Sdks> searchLikeName(String name,String value,int page,int size,String sortName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(name!=null&&!"".equals(name)&&name.length()!=0){
            name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
            queryWrapper.like(name,value);
        }
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper.orderByDesc(sortName);
        }
        Page<Sdks> pageInstance = new Page<>(page,size);
        Page<Sdks> sdksPage = sdksMapper.selectPage(pageInstance,queryWrapper);
        List records = sdksPage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",sdksPage.getTotal());
        if(records.size()!=0){
            records.add(map);
        }else{
            records = new ArrayList();
            records.add(map);
        }
        return records;
    }
    @Cacheable(cacheNames = "sdks")
    @Override
    public List<TempTagResult> selectTag() {
        return sdksMapper.selectTag();
    }
}
