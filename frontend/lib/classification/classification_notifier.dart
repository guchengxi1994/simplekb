import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'classification_state.dart';

class ClassificationNotifier
    extends AutoDisposeAsyncNotifier<ClassificationState> {
  @override
  FutureOr<ClassificationState> build() {
    // return ClassificationState(nodes: List.empty());
    return ClassificationState(nodes: [
      ClassificationTreeNode(name: "分类1", count: 10),
      ClassificationTreeNode(name: "分类2", count: 10),
      ClassificationTreeNode(name: "分类3", count: 0),
    ]);
  }
}

final classificationProvider = AutoDisposeAsyncNotifierProvider<
    ClassificationNotifier, ClassificationState>(ClassificationNotifier.new);
