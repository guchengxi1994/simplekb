package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;
import org.xiaoshuyui.simplekb.entity.KbFile;

import java.util.List;

@Data
public class FileList {
    List<KbFile> files;
    Long total;
    int pageSize;
}
