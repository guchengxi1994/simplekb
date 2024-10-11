// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'file_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FileList _$FileListFromJson(Map<String, dynamic> json) => FileList(
      files: (json['files'] as List<dynamic>)
          .map((e) => File.fromJson(e as Map<String, dynamic>))
          .toList(),
      total: (json['total'] as num).toInt(),
      pageSize: (json['pageSize'] as num).toInt(),
    );

Map<String, dynamic> _$FileListToJson(FileList instance) => <String, dynamic>{
      'files': instance.files,
      'total': instance.total,
      'pageSize': instance.pageSize,
    };

File _$FileFromJson(Map<String, dynamic> json) => File(
      id: (json['id'] as num).toInt(),
      name: json['name'] as String,
      type: (json['type'] as num).toInt(),
      createAt: json['createAt'] as String,
      updateAt: json['updateAt'] as String,
      isDeleted: (json['isDeleted'] as num).toInt(),
      chunks: (json['chunks'] as List<dynamic>)
          .map((e) => Chunk.fromJson(e as Map<String, dynamic>))
          .toList(),
      typeName: json['typeName'] as String,
    );

Map<String, dynamic> _$FileToJson(File instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'type': instance.type,
      'createAt': instance.createAt,
      'updateAt': instance.updateAt,
      'isDeleted': instance.isDeleted,
      'chunks': instance.chunks,
      'typeName': instance.typeName,
    };

Chunk _$ChunkFromJson(Map<String, dynamic> json) => Chunk(
      id: (json['id'] as num).toInt(),
      content: json['content'] as String,
      keywords:
          (json['keywords'] as List<dynamic>).map((e) => e as String).toList(),
    );

Map<String, dynamic> _$ChunkToJson(Chunk instance) => <String, dynamic>{
      'id': instance.id,
      'content': instance.content,
      'keywords': instance.keywords,
    };
