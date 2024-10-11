import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/const.dart' show getPromptsApi;
import 'package:frontend/api/dio_client.dart';
import 'package:frontend/api/models/base_model.dart';
import 'package:frontend/api/models/prompt_model.dart';
import 'package:frontend/prompt_management/prompt_management_state.dart';

class PromptManagementNotifer
    extends AutoDisposeAsyncNotifier<PromptManagementState> {
  final DioClient dioClient = DioClient();

  @override
  FutureOr<PromptManagementState> build() async {
    final res = await dioClient.get(getPromptsApi);

    Map<String, dynamic> data = res.data as Map<String, dynamic>;

    final prompts = BaseModel<List<PromptModel>>.fromJson(
        data,
        (d) => (d as List)
            .map((i) => PromptModel.fromJson(i as Map<String, dynamic>))
            .toList());

    return PromptManagementState(promptList: prompts.data ?? []);
  }
}

final promptManagementProvider = AutoDisposeAsyncNotifierProvider<
    PromptManagementNotifer,
    PromptManagementState>(PromptManagementNotifer.new);
