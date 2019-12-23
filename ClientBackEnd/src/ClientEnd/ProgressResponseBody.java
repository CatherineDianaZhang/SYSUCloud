package ClientEnd;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Http2Reader;
import okhttp3.internal.ws.RealWebSocket;
import okio.*;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ProgressResponseBody extends ResponseBody {
    public static final int UPDATE = 0x01;
    public static final String TAG = ProgressResponseBody.class.getName();
    private ResponseBody responseBody;
    private ProgressListener mListener;
    private BufferedSource bufferedSource;
    private Http2Reader.Handler myHandler;
    public ProgressResponseBody(ResponseBody body, ProgressListener listener) {
        responseBody = body;
        mListener = listener;
        if (myHandler==null){
            myHandler = (Http2Reader.Handler) new MyHandler();
        }
    }

    /**
     * 将进度放到主线程中显示
     */
    class MyHandler extends Handler {
        public MyHandler() {
            return;
        }

        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }

//        public MyHandler() {
//            super(Looper.getMainLooper());
//        }

//        @Override
//        public void handleMessage(RealWebSocket.Message msg) {
//            switch (msg.what){
//                case UPDATE:
//                    ProgressModel progressModel = (ProgressModel) msg.obj;
//                    //接口返回
//                    if (mListener!=null)mListener.onProgress(progressModel.getCurrentBytes(),progressModel.getContentLength(),progressModel.isDone());
//                    break;
//
//            }
//        }
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if (bufferedSource==null){
            bufferedSource = Okio.buffer(new ForwardingSource(this.source()) {
                @Override
                public void close() throws IOException {
                    super.close();
                }
            });
//            bufferedSource = Okio.buffer(mySource(responseBody.source()));
        }
        return bufferedSource;
    }

//    private Source mySource(Source source) {
//
//        return  new ForwardingSource(source) {
//            long totalBytesRead = 0L;
//            @Override
//            public long read(Buffer sink, long byteCount) throws IOException {
//                long bytesRead = super.read(sink, byteCount);
//                totalBytesRead +=bytesRead!=-1?bytesRead:0;
//                //发送消息到主线程，ProgressModel为自定义实体类
//                RealWebSocket.Message msg = RealWebSocket.Message.obtain();
//                msg.what = UPDATE;
//                msg.obj =  new ProgressModel(totalBytesRead,contentLength(),totalBytesRead==contentLength());
//                myHandler.sendMessage(msg);
//
//                return bytesRead;
//            }
//        };
//    }
}
//————————————————
//        版权声明：本文为CSDN博主「huang小qing」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/Gary__123456/article/details/74157403