package com.qavi.attendanceapplication.imagevideo.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@Builder
public class FileUploadResponse {
    private ArrayList<Long> successfullyUploadedFileKeys;
}
