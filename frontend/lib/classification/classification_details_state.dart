import 'package:frontend/api/models/file_model.dart';

class ClassificationDetailsState {
  final FileList fileList;
  final int pageId;

  ClassificationDetailsState({required this.fileList, this.pageId = 1});

  ClassificationDetailsState copyWith({
    FileList? fileList,
    int? pageId,
  }) {
    return ClassificationDetailsState(
      fileList: fileList ?? this.fileList,
      pageId: pageId ?? this.pageId,
    );
  }
}
