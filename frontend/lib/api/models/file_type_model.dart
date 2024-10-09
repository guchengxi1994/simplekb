import 'package:json_annotation/json_annotation.dart';

part 'file_type_model.g.dart';

@JsonSerializable()
class FileTypeList {
  final List<FileTypeModel> types;

  FileTypeList({
    required this.types,
  });

  Map<String, dynamic> toJson() => _$FileTypeListToJson(this);

  factory FileTypeList.fromJson(Map<String, dynamic> json) =>
      _$FileTypeListFromJson(json);
}

@JsonSerializable()
class FileTypeModel {
  final String name;
  final int fileCount;
  final int typeId;

  FileTypeModel({
    required this.name,
    required this.fileCount,
    required this.typeId,
  });

  Map<String, dynamic> toJson() => _$FileTypeModelToJson(this);

  factory FileTypeModel.fromJson(Map<String, dynamic> json) =>
      _$FileTypeModelFromJson(json);
}
