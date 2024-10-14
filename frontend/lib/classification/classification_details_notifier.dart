import 'dart:async';

import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/const.dart';
import 'package:frontend/api/dio_client.dart';
import 'package:frontend/api/models/file_model.dart';

import '../api/models/base_model.dart';
import 'classification_details_state.dart';

class ClassificationDetailsNotifier
    extends AutoDisposeFamilyAsyncNotifier<ClassificationDetailsState, int> {
  final DioClient dio = DioClient();

  @override
  FutureOr<ClassificationDetailsState> build(int arg) async {
    final Response res =
        await dio.get(getFilesApi, queryParameters: {"type": arg, "pageId": 1});

    Map<String, dynamic> data = res.data as Map<String, dynamic>;

    final fileList = BaseModel<FileList>.fromJson(
        data, (d) => FileList.fromJson(d as Map<String, dynamic>));
    return ClassificationDetailsState(fileList: fileList.data!);
  }

  late int maxPage =
      (state.value!.fileList.total + state.value!.fileList.pageSize - 1) ~/
          state.value!.fileList.pageSize;

  nextPage() async {
    if (state.value!.pageId < maxPage) {
      state = AsyncLoading();

      state = await AsyncValue.guard(() async {
        final Response res = await dio.get(getFilesApi,
            queryParameters: {"type": arg, "pageId": state.value!.pageId + 1});

        Map<String, dynamic> data = res.data as Map<String, dynamic>;

        final fileList = BaseModel<FileList>.fromJson(
            data, (d) => FileList.fromJson(d as Map<String, dynamic>));
        return ClassificationDetailsState(
            fileList: fileList.data!, pageId: state.value!.pageId + 1);
      });
    }
  }

  prevPage() async {
    if (state.value!.pageId > 1) {
      state = AsyncLoading();

      state = await AsyncValue.guard(() async {
        final Response res = await dio.get(getFilesApi,
            queryParameters: {"type": arg, "pageId": state.value!.pageId - 1});

        Map<String, dynamic> data = res.data as Map<String, dynamic>;

        final fileList = BaseModel<FileList>.fromJson(
            data, (d) => FileList.fromJson(d as Map<String, dynamic>));

        return ClassificationDetailsState(
            fileList: fileList.data!, pageId: state.value!.pageId - 1);
      });
    }
  }

  refresh() async {
    state = AsyncLoading();
    state = await AsyncValue.guard(() async {
      final Response res = await dio.get(getFilesApi,
          queryParameters: {"type": arg, "pageId": state.value!.pageId});

      Map<String, dynamic> data = res.data as Map<String, dynamic>;

      final fileList = BaseModel<FileList>.fromJson(
          data, (d) => FileList.fromJson(d as Map<String, dynamic>));
      return ClassificationDetailsState(fileList: fileList.data!);
    });
  }
}

final classificationDetailsProvider = AutoDisposeAsyncNotifierProvider.family<
    ClassificationDetailsNotifier,
    ClassificationDetailsState,
    int>(() => ClassificationDetailsNotifier());
