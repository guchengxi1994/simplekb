// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'file_upload_by_type_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FileUploadByTypeModel _$FileUploadByTypeModelFromJson(
        Map<String, dynamic> json) =>
    FileUploadByTypeModel(
      chunkType: json['chunkType'] as String,
      typeName: json['typeName'] as String,
      chunks: (json['chunks'] as List<dynamic>)
          .map((e) => Chunk.fromJson(e as Map<String, dynamic>))
          .toList(),
      typeId: (json['typeId'] as num).toInt(),
      filename: json['filename'] as String,
    );

Map<String, dynamic> _$FileUploadByTypeModelToJson(
        FileUploadByTypeModel instance) =>
    <String, dynamic>{
      'chunkType': instance.chunkType,
      'typeName': instance.typeName,
      'filename': instance.filename,
      'chunks': instance.chunks,
      'typeId': instance.typeId,
    };

Chunk _$ChunkFromJson(Map<String, dynamic> json) => Chunk(
      chunkId: (json['chunkId'] as num).toInt(),
      content: json['content'] as String,
      keywords:
          (json['keywords'] as List<dynamic>).map((e) => e as String).toList(),
    );

Map<String, dynamic> _$ChunkToJson(Chunk instance) => <String, dynamic>{
      'chunkId': instance.chunkId,
      'content': instance.content,
      'keywords': instance.keywords,
    };
