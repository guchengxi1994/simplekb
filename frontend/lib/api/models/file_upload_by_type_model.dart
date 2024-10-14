import 'package:json_annotation/json_annotation.dart';

part 'file_upload_by_type_model.g.dart';

@JsonSerializable()
class FileUploadByTypeModel {
  String chunkType;
  String typeName;
  String filename;
  List<Chunk> chunks;
  int typeId;

  FileUploadByTypeModel({
    required this.chunkType,
    required this.typeName,
    required this.chunks,
    required this.typeId,
    required this.filename,
  });

  factory FileUploadByTypeModel.fromJson(Map<String, dynamic> json) =>
      _$FileUploadByTypeModelFromJson(json);

  Map<String, dynamic> toJson() => _$FileUploadByTypeModelToJson(this);
}

@JsonSerializable()
class Chunk {
  int chunkId;
  String content;
  List<String> keywords;

  Chunk({
    required this.chunkId,
    required this.content,
    required this.keywords,
  });

  factory Chunk.fromJson(Map<String, dynamic> json) => _$ChunkFromJson(json);

  Map<String, dynamic> toJson() => _$ChunkToJson(this);
}
