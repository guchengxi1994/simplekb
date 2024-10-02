package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;
import org.xiaoshuyui.simplekb.documentLoader.FileTypeDetector;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ReadFileTypeTest {

    @Test
    public void test() throws IOException {
        var file = getClass().getResourceAsStream("/dbgpt.doc");
        String fileType = FileTypeDetector.getFileType(new BufferedInputStream(file));
        System.out.println(fileType);

        var file2 = getClass().getResourceAsStream("/dbgpt.docx");
        String fileType2 = FileTypeDetector.getFileType(new BufferedInputStream(file2));
        System.out.println(fileType2);

        System.out.println(FileTypeDetector.inferFileType("dbgpt.doc",fileType));
    }
}
