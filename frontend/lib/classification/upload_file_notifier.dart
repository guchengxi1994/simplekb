import 'dart:async';

import 'package:dio/dio.dart';
import 'package:file_selector/file_selector.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/const.dart'
    show fileUploadByTypePre, fileUploadByTypeSubmit;
import 'package:frontend/api/dio_client.dart';
import 'package:frontend/api/models/file_upload_by_type_model.dart';
import 'package:frontend/classification/upload_file_state.dart';
import 'package:frontend/utils.dart';

import '../api/models/base_model.dart';

class UploadFileNotifier extends AutoDisposeNotifier<UploadFileState> {
  final DioClient dioClient = DioClient();

  @override
  UploadFileState build() {
    return UploadFileState();
  }

  addKeyword(String s, int chunkId) {
    final chunk = state.data!.chunks[chunkId];
    chunk.keywords.add(s);
    state = state.copyWith(data: state.data);
  }

  removeKeyword(String s, int chunkId) {
    final chunk = state.data!.chunks[chunkId];
    chunk.keywords.remove(s);
    state = state.copyWith(data: state.data);
  }

  changeChunkType(String s, int type) async {
    if (s != state.chunkType) {
      state = state.copyWith(chunkType: s);
    }

    if (state.data != null && state.file != null) {
      // 重新访问
      state = state.copyWith(file: state.file, isLoading: true);

      FormData formData = FormData.fromMap({
        "file": MultipartFile.fromBytes(await state.file!.readAsBytes(),
            filename: state.file!.name),
        "type": type,
        "chunkType": chunkTypes.indexOf(state.chunkType)
      });
      final res = await dioClient.post(fileUploadByTypePre,
          data: formData, options: Options(contentType: 'multipart/form-data'));

      final response = BaseModel<FileUploadByTypeModel>.fromJson(res.data,
          (d) => FileUploadByTypeModel.fromJson(d as Map<String, dynamic>));

      state = state.copyWith(data: response.data, isLoading: false);
    }
  }

  Future uploadFile(int type) async {
    final XFile? file = await openFile();
    if (file == null) return;

    state = state.copyWith(file: file, isLoading: true);

    FormData formData = FormData.fromMap({
      "file": MultipartFile.fromBytes(await file.readAsBytes(),
          filename: file.name),
      "type": type,
      "chunkType": chunkTypes.indexOf(state.chunkType)
    });
    final res = await dioClient.post(fileUploadByTypePre,
        data: formData, options: Options(contentType: 'multipart/form-data'));

    final response = BaseModel<FileUploadByTypeModel>.fromJson(res.data,
        (d) => FileUploadByTypeModel.fromJson(d as Map<String, dynamic>));

    state = state.copyWith(data: response.data, isLoading: false);
  }

  Future submitUpload() async {
    if (state.data == null) return;

    state = state.copyWith(isLoading: true);

    final res = await dioClient.post(fileUploadByTypeSubmit,
        data: state.data!.toJson());

    if (res.data['code'] == 200 && res.data['success'] == true) {
      ToastUtils.sucess(null, title: "上传成功");
      state = UploadFileState();
    } else {
      ToastUtils.error(null, title: "上传失败");
      state = state.copyWith(isLoading: false);
    }
  }
}

final uploadFileProvider =
    AutoDisposeNotifierProvider<UploadFileNotifier, UploadFileState>(
        UploadFileNotifier.new);
