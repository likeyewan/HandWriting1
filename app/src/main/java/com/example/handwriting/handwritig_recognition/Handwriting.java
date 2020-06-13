package com.example.handwriting.handwritig_recognition;
import android.os.Environment;

import java.net.URLEncoder;



import com.example.handwriting.bean.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
* 手写文字识别
*/
public class Handwriting {

    /**
    * 重要提示代码中所需工具类
    * FileUtil,Base64Util,HttpUtil,GsonUtils请从
    * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
    * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
    * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
    * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
    * 下载
    */
    public String handwriting() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/handwriting";
        try {

            String filePath=Environment.getExternalStorageDirectory().getCanonicalPath().toString() +
                        "/HandWriting/Picture/"+"test.png";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            AuthService authService=new AuthService();
            
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = authService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            Gson gson2=new Gson();
            Result re  = gson2.fromJson(result, new TypeToken<Result>(){}.getType());
//            Log.d("daf","fas"+re.getWords_result().get(0).getWords());
            //System.out.println(re.getWords_result().get(0).getWords());
            StringBuffer string=new StringBuffer();
            for(int i=0;i<re.getWords_result().size();i++){
                string.append(re.getWords_result().get(i).getWords());
            }
            return "一"+string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}