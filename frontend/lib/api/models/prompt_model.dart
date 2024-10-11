import 'package:json_annotation/json_annotation.dart';

part 'prompt_model.g.dart';

@JsonSerializable()
class PromptModel {
  final int promptId;
  final String promptName;
  final String promptContent;

  PromptModel({
    required this.promptId,
    required this.promptName,
    required this.promptContent,
  });

  Map<String, dynamic> toJson() => _$PromptModelToJson(this);

  factory PromptModel.fromJson(Map<String, dynamic> json) =>
      _$PromptModelFromJson(json);
}
