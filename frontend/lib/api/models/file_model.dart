import 'package:json_annotation/json_annotation.dart';

part 'file_model.g.dart';

@JsonSerializable()
class FileList {
  final List<File> files;
  final int total;
  final int pageSize;

  FileList({
    required this.files,
    required this.total,
    required this.pageSize,
  });

  factory FileList.fromJson(Map<String, dynamic> json) =>
      _$FileListFromJson(json);
  Map<String, dynamic> toJson() => _$FileListToJson(this);
}

@JsonSerializable()
class File {
  final int id;
  final String name;
  final int type;
  final String createAt;
  final String updateAt;
  final int isDeleted;
  final List<Chunk> chunks;
  final String typeName;

  File({
    required this.id,
    required this.name,
    required this.type,
    required this.createAt,
    required this.updateAt,
    required this.isDeleted,
    required this.chunks,
    required this.typeName,
  });

  factory File.fromJson(Map<String, dynamic> json) => _$FileFromJson(json);
  Map<String, dynamic> toJson() => _$FileToJson(this);
}

@JsonSerializable()
class Chunk {
  final int id;
  final String content;
  final String? title;
  final List<String> keywords;

  Chunk({
    required this.id,
    required this.content,
    this.title,
    required this.keywords,
  });

  factory Chunk.fromJson(Map<String, dynamic> json) => _$ChunkFromJson(json);
  Map<String, dynamic> toJson() => _$ChunkToJson(this);
}
