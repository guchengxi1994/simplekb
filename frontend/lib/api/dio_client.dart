import 'package:dio/dio.dart';

class DioClient {
  static final DioClient _instance = DioClient._internal();
  late Dio dio;

  factory DioClient() {
    return _instance;
  }

  DioClient._internal() {
    dio = Dio();
  }

  Future<Response<T>> get<T>(String path,
      {Map<String, dynamic>? queryParameters}) async {
    return await dio.get<T>(path, queryParameters: queryParameters);
  }

  Future<Response<T>> post<T>(String path,
      {data, Map<String, dynamic>? queryParameters}) async {
    return await dio.post<T>(path,
        data: data, queryParameters: queryParameters);
  }

  // 其他HTTP方法（如put, delete等）可以类似地添加
}
