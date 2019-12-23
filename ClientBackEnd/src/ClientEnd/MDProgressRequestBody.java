//package ClientEnd;
//
//import okhttp3.*;
//import okio.BufferedSink;
//
//import java.io.IOException;
//
//public class MDProgressRequestBody extends FileProgressRequestBody {
//
//    protected final byte[] content;
//
//    public MDProgressRequestBody(byte[] content, String contentType , ProgressListener listener) {
//
//        this.content = content;
//
//        this.contentType = contentType;
//
//        this. listener = listener;
//
//    }
//
//    @Override
//
//    public long contentLength() {
//
//        return content.length;
//
//    }
//
//    @Override
//
//    public void writeTo(BufferedSink sink) throws IOException {
//
//        int offset = 0 ;
//
//        //计算分块数
//
//        count = (int) ( content.length / SEGMENT_SIZE + (content.length % SEGMENT_SIZE != 0?1:0) );
//
//        for( int i=0; i < count; i++ ) {
//
//            int chunk = i != count -1  ? SEGMENT_SIZE : content.length - offset;
//
//            sink.buffer().write(content, offset, chunk );//每次写入SEGMENT_SIZE 字节
//
//            sink.buffer().flush();
//
//            offset += chunk;
//
//            listener.transferred( offset );
//
//        }
//
//    }
//
//}