package x.y.z.controller;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x.y.z.entity.Apis;
import x.y.z.entity.Result;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.service.ApisService;
import x.y.z.utils.ExportExcel;
import x.y.z.utils.ReturnUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/apis")
@CrossOrigin
public class ApisController {
    @Autowired
    ApisService apisService;
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Apis> apis = apisService.selectAll();
        return new ReturnUtil<>().getOk(apis);
    }
    @GetMapping("/selectByPage")
    public Result selectByPage(int page,int size,String sortName){
        List apis = apisService.selectByPage(page,size,sortName);
        return new ReturnUtil<>().getOk(apis);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        List<Apis> apis = apisService.selectAll();
        try {
            ExportExcel.exportMashups(response,apis,"apis");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/groupQuery")
    public Result groupQuery(int num){
        List apis = apisService.groupQuery().subList(0,num);
        if(num<apis.size()){
            apis = apis.subList(0,num);
        }
        return new ReturnUtil<>().getOk(apis);
    }
    @GetMapping("/searchLikeName")
    public Result searchLikeName(String name,String value,int page,int size,String sortName){
        name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        List list = apisService.searchLikeName(name,value,page,size,sortName);
        return new ReturnUtil<>().getOk(list);
    }
    @GetMapping("/selectTag")
    public Result selectTag(int num){
        List<TempTagResult> list = apisService.selectTag();
        if(list.size()>num){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }

}
