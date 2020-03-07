package com.younger.community.provider;

import com.alibaba.fastjson.JSON;
import com.younger.community.dto.AccessTokenDto;
import com.younger.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
     2.Users are redirected back to your site by GitHub
     携带code访问github，返回access_token

     3.https://api.github.com/user
     携带access_token访问userAPI，返回user信息
 */

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDto accessTokenDto) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        //json是一种数据交互格式
        //json用来封装请求参数，数据已经通过accessTokenDto封装了
        //因此使用fastjson将bean转为json
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        //请求
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //发出请求，获取响应内容
        try (Response response = client.newCall(request).execute()) {
            //得到的string即为返回的access_token
            //access_token=523f4bc3d462a375096a1ccf1ac8e7546e24a060&scope=&token_type=bearer
            String string = response.body().string();
            //先用&拆分
            String[] split = string.split("&");
            String tokenstr = split[0];
            //再用等号拆分
            String token = tokenstr.split("=")[1];
            //System.out.println(token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //携带access_token访问userAPI，返回user信息
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        //携带accesstoken访问github user API
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        //获取返回的user
        try {
            Response response = client.newCall(request).execute();
            //获得返回的user信息，是json字符串格式
            String string = response.body().string();
            //将json转换为githubuser对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
