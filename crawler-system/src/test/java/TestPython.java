import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPython {
    @Test
    public void propertiesTest(){
        Properties prop = new Properties();
        InputStream in = null;
        FileOutputStream oFile = null;
        BufferedWriter bw = null;
        String filePath = "D:\\Python\\workplace\\crawlerData\\dist\\main\\data\\field.properties";
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            //prop.load(in);//直接这么写，如果properties文件中有汉子，则汉字会乱码。因为未设置编码格式。
            prop.load(new InputStreamReader(in, "utf-8"));
            prop.setProperty("apis.name", "apiName22");
            oFile = new FileOutputStream(filePath, false);//true表示追加打开,false每次都是清空再重写
            bw = new BufferedWriter(new OutputStreamWriter(oFile, "utf-8"));
            bw.newLine();
            for (String key : prop.stringPropertyNames()) {
                String val = prop.getProperty(key);
                System.out.println(key + ":" + prop.getProperty(key));
                bw.write(key + "=" + val);
                bw.newLine();
            }
            bw.flush();
//            //保存属性到b.properties文件
//            oFile = new FileOutputStream(filePath, false);//true表示追加打开,false每次都是清空再重写
//            System.out.println(prop.getProperty("apis.url"));
//            //prop.store(oFile, "此参数是保存生成properties文件中第一行的注释说明文字");//这个会两个地方乱码
//            //prop.store(new OutputStreamWriter(oFile, "utf-8"), "汉字乱码");//这个就是生成的properties文件中第一行的注释文字乱码
//            prop.store(new OutputStreamWriter(oFile, "utf-8"), "lll");
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
//        String filePath = "D:\\Python\\workplace\\crawlerData\\dist\\main\\data\\field.properties";
//        try {
//            FileReader fileReader = new FileReader(filePath);
//
//            Properties properties = new Properties();
//            try {
//                properties.load(fileReader);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(properties.get("mashups.name"));
//            properties.setProperty("mashups.name","中文22");
//            try {
//                properties.store(new FileOutputStream(filePath),"");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(properties.get("mashups.name"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
////        Properties properties = new Properties();
////        properties.setProperty("apis.name","1234APi");
////        try {
////            properties.store(new FileOutputStream(filePath),"");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String absolutePath = ResourceUtils.getFile("classpath:bat").getAbsolutePath();
        String completePath = absolutePath + "\\run.bat";
        System.out.println(completePath);
        ProcessBuilder processBuilder = new ProcessBuilder(completePath);
        StringBuilder stringBuilder = new StringBuilder();
        processBuilder.redirectErrorStream(true);
        Process process =null;
//        String arguments = "D:\\Python\\workplace\\crawlerData\\dist\\main\\main.exe";
//        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
//        StringBuilder stringBuilder = new StringBuilder();
//        processBuilder.redirectErrorStream(true);
//        Process process =null;
        try {
            // 获取程序执行后返回的结果
            //执行这个.exe程序
//            process = processBuilder.start();
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
    @Test
    public void TestEXEC(){


    }

    @Test
    public void test1() {
        Process proc;
//        try {
//            String absolutePath = ResourceUtils.getFile("classpath:bat").getAbsolutePath();
//            System.out.println(absolutePath);
//            String completePath = absolutePath + "\\run.bat";
//            System.out.println(completePath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        try {
              String absolutePath = ResourceUtils.getFile("classpath:bat").getAbsolutePath();
              String completePath = absolutePath + "\\run.bat";
              System.out.println(completePath);
            proc = Runtime.getRuntime().exec("cmd /c "+completePath);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        System.out.print(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testData"));
    }
    @Test
    public void test3(){
        try {
            String absolutePath = ResourceUtils.getFile("classpath:crawler1.0").getAbsolutePath();
            Desktop.getDesktop().open(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){
        String appid = "20220508001207513";
        String securityKey = "gloHLjM2Ymq1JneFjN9T";
        TransApi transApi = new TransApi(appid,securityKey);
//        String query = "你好，世界";
//        String from = "zh";
//        String to = "en";
        String query = "My name is Xiao Ming";
        String from = "en";
        String to = "zh";
        String transResult = transApi.getTransResult(query, from, to);
        System.out.println(transResult);
        String s = unicodeDecode(transResult);
        System.out.println(s);
    }

    /**
     *
     * @param string
     * @return
     */
    public  String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);

        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
