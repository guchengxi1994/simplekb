package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

/**
 * Represents the response after file upload, providing information on the upload status and the unique identifier of the file.
 */
@Data
public class UploadFileResponse {
    // The current stage of the file upload, indicating the progress status of the upload
    String stage;

    // Indicates whether the file upload is completed
    boolean done;

    // The unique identifier of the uploaded file, used to uniquely identify a file
    String uuid;
}

