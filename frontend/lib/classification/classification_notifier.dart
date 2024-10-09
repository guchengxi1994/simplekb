import 'dart:async';

import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/dio_client.dart';
import 'package:frontend/api/models/base_model.dart';
import 'package:frontend/api/const.dart' show getFileTypesApi;

import '../api/models/file_type_model.dart';
import 'classification_state.dart';

class ClassificationNotifier
    extends AutoDisposeAsyncNotifier<ClassificationState> {
  final DioClient dio = DioClient();

  @override
  FutureOr<ClassificationState> build() async {
    final Response res = await dio.get(getFileTypesApi);

    Map<String, dynamic> data = res.data as Map<String, dynamic>;

    final types = BaseModel<FileTypeList>.fromJson(
        data, (d) => FileTypeList.fromJson(d as Map<String, dynamic>));

    if (types.success && types.data != null) {
      return ClassificationState(
          nodes: types.data!.types
              .map((e) => ClassificationTreeNode(
                  name: e.name, count: e.fileCount, id: e.typeId))
              .toList());
    }

    return ClassificationState(nodes: List.empty());
  }
}

final classificationProvider = AutoDisposeAsyncNotifierProvider<
    ClassificationNotifier, ClassificationState>(ClassificationNotifier.new);
