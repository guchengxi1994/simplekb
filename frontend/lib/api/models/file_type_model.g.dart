// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'file_type_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FileTypeList _$FileTypeListFromJson(Map<String, dynamic> json) => FileTypeList(
      types: (json['types'] as List<dynamic>)
          .map((e) => FileTypeModel.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$FileTypeListToJson(FileTypeList instance) =>
    <String, dynamic>{
      'types': instance.types,
    };

FileTypeModel _$FileTypeModelFromJson(Map<String, dynamic> json) =>
    FileTypeModel(
      name: json['name'] as String,
      fileCount: (json['fileCount'] as num).toInt(),
      typeId: (json['typeId'] as num).toInt(),
    );

Map<String, dynamic> _$FileTypeModelToJson(FileTypeModel instance) =>
    <String, dynamic>{
      'name': instance.name,
      'fileCount': instance.fileCount,
      'typeId': instance.typeId,
    };
