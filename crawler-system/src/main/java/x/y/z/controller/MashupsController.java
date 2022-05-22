package x.y.z.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x.y.z.entity.Mashups;
import x.y.z.entity.Result;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.service.MashupsService;
import x.y.z.utils.ExportExcel;
import x.y.z.utils.ReturnUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequestMapping("/mashups")
@CrossOrigin
public class MashupsController{
    @Autowired
    MashupsService mashupsService;
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Mashups> mashups = mashupsService.selectAll();
        return new ReturnUtil<>().getOk(mashups);
    }
    @GetMapping("/selectByPage")
    public Result selectByPage(int page,int size,String sortName){
        List mashups = mashupsService.selectByPage(page,size,sortName);
        return new ReturnUtil<>().getOk(mashups);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        List<Mashups> mashups = mashupsService.selectAll();
        try {
            ExportExcel.exportMashups(response,mashups,"mashups");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/groupQuery")
    public Result groupQuery(int num){
        List mashups = mashupsService.groupQuery();
        if(num<mashups.size()){
            mashups = mashups.subList(0,num);
        }
        return new ReturnUtil<>().getOk(mashups);
    }
    @GetMapping("/selectStaticApis")
    public Result selectStaticApis(int num){
        List list = mashupsService.selectStatisticsApis();
        if(num<list.size()){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }
    @GetMapping("/searchLikeName")
    public Result searchLikeName(String name,String value,int page,int size,String sortName){
        List list = mashupsService.searchLikeName(name,value,page,size,sortName);
        return new ReturnUtil<>().getOk(list);
    }

    @GetMapping("/selectTag")
    public Result selectTag(int num){
        List<TempTagResult> list = mashupsService.selectTag();
        if(list.size()>num){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }
}
