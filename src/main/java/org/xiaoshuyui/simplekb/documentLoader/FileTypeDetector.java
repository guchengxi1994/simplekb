package org.xiaoshuyui.simplekb.documentLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import org.apache.tika.Tika;

public class FileTypeDetector {
    static Tika tika = new Tika();
    /**
     * 通过文件输入流检测文件类型
     *
     * @param inputStream 文件的输入流，用于读取文件头信息
     * @return 返回检测到的文件类型字符串
     * @throws IOException 如果读取文件时发生错误，则抛出IO异常
     */
    public static String getFileType(BufferedInputStream inputStream) throws IOException {
        inputStream.mark(10); // 标记文件流开始位置，以便后续重置流的位置
        byte[] fileSignature = new byte[10]; // 创建一个10字节的数组，用于存储文件的签名字节
        inputStream.read(fileSignature); // 读取文件的前10个字节到fileSignature数组中
        inputStream.reset(); // 重置流位置到标记位置，以保持流的正确读取位置

        String detectedType = tika.detect(fileSignature);
        // 使用Apache Tika库检测文件类型，根据文件签名字节检测文件类型
        // TODO
        // 有可能得到 application/x-tika-msoffice 或者 application/zip 这个类型
        // 因为早期office的文件头 signature是相同的，而现在的office文件又都是压缩包格式，
        // 所以需要根据文件后缀来判断
        return detectedType; // 返回检测到的文件类型
    }

    public static String inferFileType(String fileName,String detectedType) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return switch (detectedType) {
            case "application/x-tika-msoffice", "application/zip" -> fileExtension;
            case "text/plain" -> "txt";
            case "application/pdf" -> "pdf";
            default -> "";
        };
    }
}
