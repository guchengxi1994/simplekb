import 'package:json_annotation/json_annotation.dart';

part 'base_model.g.dart'; // 声明生成的部分

@JsonSerializable(genericArgumentFactories: true)
class BaseModel<T> {
  bool? success;
  String? message;
  T? data;
  int? timestamp;
  int? code;

  @Deprecated("use data instead")
  T? result;

  BaseModel({
    this.success,
    this.message,
    this.data,
    this.timestamp,
    this.code,
    @Deprecated("use data instead") this.result,
  });

  factory BaseModel.fromJson(Map<String, dynamic> json,
      T Function(Object? json) fromJsonT,) =>
      _$BaseModelFromJson(json, fromJsonT);

  Map<String, dynamic> toJson(Object Function(T value) toJsonT) =>
      _$BaseModelToJson(this, toJsonT);
}
