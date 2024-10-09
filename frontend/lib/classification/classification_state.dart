class ClassificationState {
  List<ClassificationTreeNode> nodes;

  ClassificationState({required this.nodes});

  ClassificationState copyWith({
    List<ClassificationTreeNode>? nodes,
  }) {
    return ClassificationState(
      nodes: nodes ?? this.nodes,
    );
  }
}

class ClassificationTreeNode {
  String name;
  int count;
  int id;

  ClassificationTreeNode(
      {required this.name, required this.count, required this.id});
}
