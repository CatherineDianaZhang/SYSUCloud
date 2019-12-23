package ClientEnd;

import okhttp3.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class HttpUploader {
    private static final int STATUS_RETRY = 400;
    private static final int STATUS_CANCEL = 500;
    private static final int STATUS_FAILED_EXIT = 401;
    protected Request request;
    public String fileName;
//    public OkHttpClient OkHttpClientMgr;
    private boolean Canceled;


    protected Request generateRequest(String url){

        // 构造上传请求，模拟表单提交文件
        String formData = String.format("form-data;name=file; filename=%s", fileName );
        FileProgressRequestBody filePart = new FileProgressRequestBody(new File(fileName), "application/octet-stream", new FileProgressRequestBody.ProgressListener() {
            @Override
            public void transferred(long size) {

            }
        });
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart( Headers.of("Content-Disposition",formData), filePart )
                .build();

        // 创建Request对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return request;
    }

    protected int doUpload(String url){
        try {
            OkHttpClient httpClient = new OkHttpClient();
//            OkHttpClient httpClient = OkHttpClientMgr.Instance().getOkHttpClient();
//            call = httpClient.newCall( generateRequest(url) );
            Response response = httpClient .newCall(this.generateRequest(url)).execute();
            if (response.isSuccessful()) {
                StringBuilder sbFileUUID = new StringBuilder();
//                return readResponse(response,sbFileUUID);
                return response.code();
            } else                                                                                                                                                                                                                                                                                                                                                   { // 重试
                return STATUS_RETRY;
            }
        } catch (IOException ioe) {
//            LogUtil.e(LOG_TAG, "exception occurs while uploading file!",ioe);
            ioe.printStackTrace();
        }
        return isCancelled() ? STATUS_CANCEL : STATUS_FAILED_EXIT;
    }

    private boolean isCancelled() {
        return this.Canceled;
    }
}