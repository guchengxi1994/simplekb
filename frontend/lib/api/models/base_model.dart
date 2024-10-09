import 'package:json_annotation/json_annotation.dart';

part 'base_model.g.dart'; // 声明生成的部分

@JsonSerializable(genericArgumentFactories: true)
class BaseModel<T> {
  final bool success;
  String? message;
  T? data;
  final int timestamp;
  int? code;

  @Deprecated("use data instead")
  T? result;

  BaseModel({
    required this.success,
    this.message,
    this.data,
    required this.timestamp,
    this.code,
    @Deprecated("use data instead") this.result,
  });

  factory BaseModel.fromJson(
    Map<String, dynamic> json,
    T Function(Object? json) fromJsonT,
  ) =>
      _$BaseModelFromJson(json, fromJsonT);

  Map<String, dynamic> toJson(Object Function(T value) toJsonT) =>
      _$BaseModelToJson(this, toJsonT);
}
