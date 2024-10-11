// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'prompt_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PromptModel _$PromptModelFromJson(Map<String, dynamic> json) => PromptModel(
      promptId: (json['promptId'] as num).toInt(),
      promptName: json['promptName'] as String,
      promptContent: json['promptContent'] as String,
    );

Map<String, dynamic> _$PromptModelToJson(PromptModel instance) =>
    <String, dynamic>{
      'promptId': instance.promptId,
      'promptName': instance.promptName,
      'promptContent': instance.promptContent,
    };
