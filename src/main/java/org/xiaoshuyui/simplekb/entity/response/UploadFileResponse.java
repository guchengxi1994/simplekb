package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

@Data
public class UploadFileResponse {
    String stage;
    boolean done;
    String uuid;
}
