package x.y.z.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x.y.z.entity.Result;
import x.y.z.entity.SampleSourceCode;
import x.y.z.entity.temp.TempTagResult;
import x.y.z.service.SampleSourceCodeService;
import x.y.z.utils.ExportExcel;
import x.y.z.utils.ReturnUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
@RestController()
@RequestMapping("/sampleSourceCode")
@CrossOrigin
public class SampleSourceCodeController {
    @Autowired
    SampleSourceCodeService sampleSourceCodeService;
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<SampleSourceCode> sdks = sampleSourceCodeService.selectAll();
        return new ReturnUtil<>().getOk(sdks);
    }
    @GetMapping("/selectByPage")
    public Result selectByPage(int page,int size,String sortName){
        List sampleSourceCodes = sampleSourceCodeService.selectByPage(page,size,sortName);
        return new ReturnUtil<>().getOk(sampleSourceCodes);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        List<SampleSourceCode> sampleSourceCodes = sampleSourceCodeService.selectAll();
        try {
            ExportExcel.exportMashups(response,sampleSourceCodes,"sampleSourceCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/groupQuery")
    public Result groupQuery(int num){
        List sampleSourceCode = sampleSourceCodeService.groupQuery();
        if(num<=sampleSourceCode.size()){
            sampleSourceCode = sampleSourceCode.subList(0,num);
        }
        return new ReturnUtil<>().getOk(sampleSourceCode);
    }
    @GetMapping("/selectStaticApis")
    public Result selectStaticApis(int num){
        List list = sampleSourceCodeService.selectStatisticsApis();
        if(num<list.size()){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }
    @GetMapping("/searchLikeName")
    public Result searchLikeName(String name,String value,int page,int size,String sortName){
        List list = sampleSourceCodeService.searchLikeName(name,value,page,size,sortName);
        return new ReturnUtil<>().getOk(list);
    }
    @GetMapping("/selectTag")
    public Result selectTag(int num){
        List<TempTagResult> list = sampleSourceCodeService.selectTag();
        if(list.size()>num){
            list = list.subList(0,num);
        }
        return new ReturnUtil<>().getOk(list);
    }
}
