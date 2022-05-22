package x.y.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import x.y.z.entity.SampleSourceCode;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempResult2;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.mapper.SampleSourceCodeMapper;
import x.y.z.service.SampleSourceCodeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SampleSourceCodeServiceImpl implements SampleSourceCodeService {
    @Autowired
    private SampleSourceCodeMapper sampleSourceCodeMapper;
    @Cacheable(cacheNames = "sampleSourceCode")
    @Override
    public List<SampleSourceCode> selectAll() {
        return sampleSourceCodeMapper.selectList(null);
    }

    @Cacheable(cacheNames = "sampleSourceCode")
    @Override
    public List<SampleSourceCode> selectByPage(int page, int size,String sortName) {
        Page<SampleSourceCode> pageInstance = new Page<>(page,size);
        QueryWrapper queryWrapper = null;
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper = new QueryWrapper();
            queryWrapper.orderByDesc(sortName);
        }
        Page<SampleSourceCode> sampleSourceCodePage = sampleSourceCodeMapper.selectPage(pageInstance, null);
        List records = sampleSourceCodePage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",sampleSourceCodePage.getTotal());
        records.add(map);
        return records;
    }

    @Cacheable(cacheNames = "sampleSourceCode")
    @Override
    public Long getCount() {
        return sampleSourceCodeMapper.selectCount(null);
    }

    @Cacheable(cacheNames = "sampleSourceCode")
    @Override
    public List<TempResult> groupQuery() {
        return sampleSourceCodeMapper.groupQuery();
    }

    @Cacheable(cacheNames = "sampleSourceCode")
    @Override
    public List<TempResult2> selectStatisticsApis() {
        return sampleSourceCodeMapper.selectStatisticsApis();
    }

    @Override
    @Cacheable(cacheNames = "sampleSourceCode")
    public List<SampleSourceCode> searchLikeName(String name,String value,int page,int size,String sortName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(name!=null&&!"".equals(name)&&name.length()!=0){
            name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
            queryWrapper.like(name,value);
        }
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper.orderByDesc(sortName);
        }
        Page<SampleSourceCode> pageInstance = new Page<>(page,size);
        Page<SampleSourceCode> samplePage = sampleSourceCodeMapper.selectPage(pageInstance,queryWrapper);
        List records = samplePage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",samplePage.getTotal());
        if(records.size()!=0){
            records.add(map);
        }else{
            records = new ArrayList();
            records.add(map);
        }
        return records;
    }
    @Cacheable(cacheNames = "sampleSourceCode")
    @Override
    public List<TempTagResult> selectTag() {
        return sampleSourceCodeMapper.selectTag();
    }
}
