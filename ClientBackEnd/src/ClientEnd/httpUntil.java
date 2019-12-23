package ClientEnd;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class httpUntil {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .writeTimeout(10000,TimeUnit.MILLISECONDS).build();

    //下载文件方法
    public static void downloadFile(String url, final ProgressListener listener, Callback callback){
        //增加拦截器
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body(),listener)).build();
            }
        }).build();

        Request request  = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
//————————————————
//    版权声明：本文为CSDN博主「huang小qing」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/Gary__123456/article/details/74157403
}
