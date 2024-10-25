package org.xiaoshuyui.simplekb;

import com.hankcs.hanlp.corpus.io.IIOAdapter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class ResourceFileIoAdapter implements IIOAdapter {
    @Override
    public InputStream open(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        // return Files.newInputStream(resource.getFile().toPath());
        // Linux环境下跑要把open()里改成这样：
        return resource.getInputStream();
    }

    @Override
    public OutputStream create(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return Files.newOutputStream(resource.getFile().toPath());
    }

}
