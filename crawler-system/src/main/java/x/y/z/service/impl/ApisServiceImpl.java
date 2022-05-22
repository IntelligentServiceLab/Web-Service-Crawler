package x.y.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import x.y.z.entity.Apis;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.mapper.ApisMapper;
import x.y.z.service.ApisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApisServiceImpl implements ApisService {
    @Autowired
    private ApisMapper apisMapper;

    @Cacheable(cacheNames = "apis")
    @Override
    public List<Apis> selectAll() {
        return apisMapper.selectList(null);
    }

    @Cacheable(cacheNames = "apis")
    @Override
    public List<Apis> selectByPage(int page, int size,String sortName) {
        Page<Apis> pageInstance = new Page<>(page,size);
        QueryWrapper queryWrapper = null;
        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper = new QueryWrapper();
            queryWrapper.orderByDesc(sortName);
        }
        Page<Apis> apisPage = apisMapper.selectPage(pageInstance, queryWrapper);
        List records = apisPage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",apisPage.getTotal());
        records.add(map);
        return records;
    }

    @Cacheable(cacheNames = "apis")
    @Override
    public Long getCount() {
        return apisMapper.selectCount(null);
    }

    @Cacheable(cacheNames = "apis")
    @Override
    public List<TempResult> groupQuery() {
        return apisMapper.groupQuery();
    }
    @Cacheable(cacheNames = "apis")
    @Override
    public List<Apis> searchLikeName(String name,String value, int page, int size,String sortName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(name!=null&&!"".equals(name)&&name.length()!=0){
            name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
            queryWrapper.like(name,value);
        }

        if(sortName!=null&&!"".equals(sortName)&&sortName.length()!=0){
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            queryWrapper.orderByDesc(sortName);
        }
        Page<Apis> pageInstance = new Page<>(page,size);
        Page<Apis> apisPage = apisMapper.selectPage(pageInstance,queryWrapper);

        List records = apisPage.getRecords();
        Map<String,Long> map = new HashMap<>();
        map.put("total",apisPage.getTotal());
        if(records.size()!=0){
            records.add(map);
        }else{
            records = new ArrayList();
            records.add(map);
        }
        return records;
    }
    @Cacheable(cacheNames = "apis")
    @Override
    public List<TempTagResult> selectTag() {
        return apisMapper.selectTag();
    }
}
