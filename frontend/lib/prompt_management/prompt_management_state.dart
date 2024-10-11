import 'package:frontend/api/models/prompt_model.dart';

class PromptManagementState {
  final List<PromptModel> promptList;

  PromptManagementState({required this.promptList});

  PromptManagementState copyWith({List<PromptModel>? promptList}) {
    return PromptManagementState(
      promptList: promptList ?? this.promptList,
    );
  }
}
