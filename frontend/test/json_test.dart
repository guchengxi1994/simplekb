// ignore_for_file: avoid_print

import 'dart:convert';

import 'package:frontend/api/base_model.dart';

class User {
  String? name;
  String? email;
  String? password;

  User({this.name, this.email, this.password});

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      name: json['name'],
      email: json['email'],
      password: json['password'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'email': email,
      'password': password,
    };
  }
}

void main() {
  final jsonString =
      '{"success":true,"message":"OK","data":{"password":"1","name":"Test","email":"123@321.com"},"timestamp":1609459200,"code":200}';
  final Map<String, dynamic> jsonMap = json.decode(jsonString);

  final baseModel = BaseModel<User>.fromJson(
      jsonMap, (data) => User.fromJson(data as Map<String, dynamic>));

  print(baseModel.data?.name);
}
