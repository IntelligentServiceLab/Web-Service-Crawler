package x.y.z.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x.y.z.entity.Result;
import x.y.z.service.ApisService;
import x.y.z.service.MashupsService;
import x.y.z.service.SampleSourceCodeService;
import x.y.z.service.SdksService;
import x.y.z.utils.ReturnUtil;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@CrossOrigin
@RequestMapping("/common")
public class CommonController {
    @Autowired
    ApisService apisService;
    @Autowired
    MashupsService mashupsService;
    @Autowired
    SampleSourceCodeService sampleSourceCodeService;
    @Autowired
    SdksService sdksService;
    private String filePath = "D:\\Python\\workplace\\crawlerData\\dist\\main";
    @GetMapping("/getCount")
    public Result getCount(){
        Long apiCount = apisService.getCount();
        Long mashupsCount = mashupsService.getCount();
        Long sampleSourceCodeCount = sampleSourceCodeService.getCount();
        Long sdksCount = sdksService.getCount();
        Map<String,Long> map = new HashMap<>();
        map.put("apisCount",apiCount);
        map.put("mashupsCount",mashupsCount);
        map.put("sampleSourceCodeCount",sampleSourceCodeCount);
        map.put("sdksCount",sdksCount);

        return new ReturnUtil<>().getOk(map,"获取成功");
    }
    @GetMapping("/executePython")
    public Result<String> executePython(String preName,String fileName,boolean flag){
        Properties prop = new Properties();
        InputStream in = null;
        FileOutputStream oFile = null;
        String completePath = filePath+"\\data\\field.properties";
        BufferedWriter bw = null;
        try {
            in = new BufferedInputStream(new FileInputStream(completePath));
            prop.load(new InputStreamReader(in, "utf-8"));
            //为了清空上次的操作，所以先把所有的flag设置为false
            prop.setProperty("flag", String.valueOf(false));
            prop.setProperty("apis.flag", String.valueOf(false));
            prop.setProperty("apis.name", "apis");
            prop.setProperty("mashups.flag", String.valueOf(false));
            prop.setProperty("mashups.name", "mashups");
            prop.setProperty("sampleSourceCode.flag", String.valueOf(false));
            prop.setProperty("sampleSourceCode.name", "sampleSourceCode");
            prop.setProperty("sdks.flag", String.valueOf(false));
            prop.setProperty("sdks.name", "sdks");

            if(preName!=null&&!"".equals(preName)&&preName.length()!=0){
                //然后根据传过来的参数进行相应值的设置
                System.out.println("定制化操作");
                prop.setProperty("flag", String.valueOf(flag));
                prop.setProperty(preName+".name", fileName);
                prop.setProperty(preName+".flag", String.valueOf(flag));
            }
            oFile = new FileOutputStream(completePath, false);//true表示追加打开,false每次都是清空再重写
            bw = new BufferedWriter(new OutputStreamWriter(oFile, "utf-8"));
            bw.newLine();
            for (String key : prop.stringPropertyNames()) {
                String val = prop.getProperty(key);
                System.out.println(key + ":" + prop.getProperty(key));
                bw.write(key + "=" + val);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (oFile != null) {
                try {
                    oFile.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这是使用绝对路径修改Python程序的.properties文件,主要是相对路径没有找到对应的办法，暂时使用绝对路径
                //这里面.bat文件也是使用绝对路径，如果Python程序位置改变，需要进行手动调整.bat文件中的路径
                String absolutePath = null;
                try {
                    absolutePath = ResourceUtils.getFile("classpath:bat").getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String completePath = absolutePath + "\\run.bat";
                System.out.println(completePath);
                ProcessBuilder processBuilder = new ProcessBuilder(completePath);
                StringBuilder stringBuilder = new StringBuilder();
                processBuilder.redirectErrorStream(true);
                Process process =null;
//                String arguments = "D:\\Python\\workplace\\crawlerData\\dist\\main\\main.exe";
//                ProcessBuilder processBuilder = new ProcessBuilder(arguments);
//                StringBuilder stringBuilder = new StringBuilder();
//                processBuilder.redirectErrorStream(true);
//                Process process =null;
                try {
                    // 获取程序执行后返回的结果
                    //执行这个.exe程序
//                    process = processBuilder.start();
                    process = Runtime.getRuntime().exec("cmd /c start " + completePath);
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
                    String line = null;
                    while ((line = in.readLine()) != null) {
//                System.out.println(line);
                        stringBuilder.append(line + System.lineSeparator());
                    }
                    in.close();
                    //java代码中的process.waitFor()返回值为0表示我们执行.exe文件成功，
                    //返回值为1表示执行.exe文件失败，这和我们通常意义上见到的0与1定义正好相反
                    int re = process.waitFor();
                    System.out.println(re);
                    System.out.println(stringBuilder.toString().trim()+"-------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if (process != null) {
                        process.destroy();
                    }
                }
            }
        }).start();
        return new ReturnUtil<String>().getOk("程序开始执行");
    }
    @GetMapping("/openDir")
    public Result<String> openDir(){
        //这个代码是打开main.exe的目录
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ReturnUtil<String>().getOk("点击main.exe文件可以执行爬虫程序");
    }
}
