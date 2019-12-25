package ClientEnd;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import okhttp3.*;
import com.alibaba.fastjson.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientEnd extends Thread {
    private static String url = "http://cloud.sysu.rwong.tech";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                // Just for simply store JSESSIONID
                private List<Cookie> sessionCookie = new LinkedList<>();
                @Override
                public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                    sessionCookie = list;
                }

                @NotNull
                @Override
                public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                    return sessionCookie;
                }
            })
            .connectTimeout(1, TimeUnit.DAYS)
            .writeTimeout(1, TimeUnit.DAYS)
            .readTimeout(1, TimeUnit.DAYS)
            .build();
    private static int port = 8080;

    public String getUrl() {return this.url;}
    public int getPort() {return this.port;}
    public OkHttpClient getClient() {return this.client;}

    public void logOut(CallBackFunc callBackFunc) throws Exception {
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/user/log_out")
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    callBackFunc.done(new CallBackFunArg(true, null, null));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.body().close();
                }
            }
        });

    }

    public void signIn(String username, String password, CallBackFunc callBackFunc) throws Exception {
        final MediaType JSON = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/user/sign_in")
                .post(RequestBody.create(JSON, json.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                switch (response.code()) {
                    case 200:
                        try {
                            callBackFunc.done(new CallBackFunArg(true, null, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 401:
                        try {
                            callBackFunc.done(new CallBackFunArg(false, null, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 500: throw new IOException("后端错误");
                    case 400: throw new IOException("用户密码错误");
                    default: throw new IOException("我也不知道什么问题: " + response.code());
                }
                response.body().close();
            }
        });

    }

    public void signUp(String username, String password, String email, String name, CallBackFunc callBackFunc) throws Exception {
        final MediaType JSON = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        json.put("email", email);
        json.put("name", name);
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/user/sign_up")
                .post(RequestBody.create(JSON, json.toString()))
                .build();
        System.out.println("准备注册");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                switch (response.code()) {
                    case 200:
                        try {
                            callBackFunc.done(new CallBackFunArg(true, null, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } break;
                    case 400:
                        try {
                            callBackFunc.done(new CallBackFunArg(false, null, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } break;
                    case 500: throw new IOException(("后端错误"));
                    case 401: throw new IOException("用户已经存在");
                    default: throw new IOException("我也不知道什么问题: " + response.code());
                }
                response.body().close();
            }
        });

    }

    public void getFileList(String path, CallBackFunc callBackFunc) throws Exception {
//        final MediaType JSON = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/folders" + path)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    callBackFunc.done(new CallBackFunArg(true, JSON.parseObject(body.string()), null));
                    body.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void createFolder(String path, CallBackFunc callBackFunc) throws Exception {
        Request request = new Request.Builder()
                .url(this.getUrl() + ':' + this.getPort() + "/folders" + path)
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    callBackFunc.done(new CallBackFunArg(true, null, null));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.body().close();
                }
            }
        });
    }

    public void getFileDetails(int fileId, CallBackFunc callBackFunc) throws Exception {
        final MediaType JSON = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/files/" + fileId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    callBackFunc.done(new CallBackFunArg(false, JSONObject.parseObject(body.string()), null));
                    body.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void delFile(int fileId, CallBackFunc callBackFunc) throws Exception {
        final MediaType JSON = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/files/" + fileId)
                .delete(new FormBody.Builder().build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    callBackFunc.done(new CallBackFunArg(response.code() == 200, null, null));
                    response.body().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void download(int fileId, String savePath, CallBackFunc callBackFunc, CallBackFunc progressLength) throws Exception {
        Request request = new Request.Builder()
                .url(this.url + ':' + this.port + "/files/" + fileId +"/download/")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                try {
                    callBackFunc.done(new CallBackFunArg(false, null, null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                int len = 0;
                byte[] buf = new byte[2048];
                try {
                    ResponseBody body = response.body();
                    assert body != null;
                    is = body.byteStream();
                    File file = new File(savePath);
                    fos = new FileOutputStream(file);
                    long total = body.contentLength();
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        JSONObject pLength = new JSONObject();
                        pLength.put("length", progress);
                        progressLength.done(new CallBackFunArg(true, pLength, null));
                        // try to finish progress length
                    }
                    fos.flush();
                    fos.close();
                    body.close();
                    callBackFunc.done(new CallBackFunArg(true, null, null));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    try {
                        callBackFunc.done(new CallBackFunArg(false, null, null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void upload(File file, String fullPath, CallBackFunc callBackFunc, CallBackFunc progressLength) throws Exception {
            MagicMatch match = Magic.getMagicMatch(file, false);
            String fileType = match.getMimeType();

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            new CountingFileRequestBody(file, fileType, new CountingFileRequestBody.ProgressListener() {
                                @Override
                                public void transferred(int percent) {
                                    JSONObject pLength = new JSONObject();
                                    pLength.put("length", percent);
                                    try {
                                        progressLength.done(new CallBackFunArg(true, pLength, null));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }))
                    .addFormDataPart("fullPath", fullPath + file.getName())
                    .build();

            Request request = new Request.Builder()
                    .url(this.url + ':' + this.port + "/files")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                    try {
                        callBackFunc.done(new CallBackFunArg(false, null, null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        callBackFunc.done(new CallBackFunArg(true, null, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        response.body().close();
                    }
                }
            });

    }
}
