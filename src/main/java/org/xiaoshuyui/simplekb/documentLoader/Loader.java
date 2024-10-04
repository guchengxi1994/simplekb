package org.xiaoshuyui.simplekb.documentLoader;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.documentLoader.result.Result;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class Loader {

    @Resource
    private DocLoader docLoader;

    @Resource
    private CommonLoader commonLoader;

    public Result load(String fileName, InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        String fileType = FileTypeDetector.getFileType(bis);
        String detectedType = FileTypeDetector.inferFileType(fileName, fileType);

        switch (detectedType) {
            case "doc":
                return docLoader.extract(bis);
            default:
                return commonLoader.extract(bis);
        }
    }
}
