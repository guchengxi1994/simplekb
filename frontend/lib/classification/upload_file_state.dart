import 'package:file_selector/file_selector.dart';
import 'package:frontend/api/models/file_upload_by_type_model.dart';

const List<String> chunkTypes = ["不切分", "固定长度切分", "标题增强切分"];

class UploadFileState {
  final bool isLoading;
  final FileUploadByTypeModel? data;
  final XFile? file;
  final String chunkType;

  UploadFileState({
    this.isLoading = false,
    this.data,
    this.file,
    this.chunkType = "不切分",
  });

  UploadFileState copyWith({
    bool? isLoading,
    FileUploadByTypeModel? data,
    XFile? file,
    String? chunkType,
  }) {
    return UploadFileState(
      isLoading: isLoading ?? this.isLoading,
      data: data ?? this.data,
      file: file ?? this.file,
      chunkType: chunkType ?? this.chunkType,
    );
  }
}
