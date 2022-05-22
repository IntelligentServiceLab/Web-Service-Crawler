package x.y.z.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x.y.z.entity.Result;
import x.y.z.entity.Sdks;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.service.SdksService;
import x.y.z.utils.ExportExcel;
import x.y.z.utils.ReturnUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
@RestController()
@RequestMapping("/sdks")
@CrossOrigin
public class SdksController {
    @Autowired
    SdksService sdksService;
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Sdks> sdks = sdksService.selectAll();
        return new ReturnUtil<>().getOk(sdks);
    }
    @GetMapping("/selectByPage")
    public Result selectByPage(int page,int size,String sortName){
        List mashups = sdksService.selectByPage(page,size,sortName);
        return new ReturnUtil<>().getOk(mashups);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        List<Sdks> sdks = sdksService.selectAll();
        try {
            ExportExcel.exportMashups(response,sdks,"sdks");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/groupQuery")
    public Result groupQuery(int num){
        List sdks = sdksService.groupQuery();
        if(num<sdks.size()){
            sdks = sdks.subList(0,num);
        }
        return new ReturnUtil<>().getOk(sdks);
    }
    @GetMapping("/selectStaticApis")
    public Result selectStaticApis(int num){
        List list = sdksService.selectStatisticsApis();
        if(num<list.size()){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }
    @GetMapping("/searchLikeName")
    public Result searchLikeName(String name,String value,int page,int size,String sortName){
        List list = sdksService.searchLikeName(name,value,page,size,sortName);
        return new ReturnUtil<>().getOk(list);
    }
    @GetMapping("/selectTag")
    public Result selectTag(int num){
        List<TempTagResult> list = sdksService.selectTag();
        if(list.size()>num){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }
}
