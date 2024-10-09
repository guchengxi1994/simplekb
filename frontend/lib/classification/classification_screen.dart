import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:treemap/treemap.dart';

import 'classification_notifier.dart';

class ClassificationScreen extends ConsumerWidget {
  const ClassificationScreen({super.key});

  static Random random = Random();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(classificationProvider);

    return state.when(
        data: (v) {
          return Container(
            padding: EdgeInsets.all(20),
            width: MediaQuery
                .of(context)
                .size
                .width,
            height: MediaQuery
                .of(context)
                .size
                .height,
            child: TreeMapLayout(
              duration: Duration(milliseconds: 500),
              tile: Squarify(),
              children: [
                TreeNode.node(
                    children: v.nodes
                        .map(
                          (n) =>
                          TreeNode.leaf(
                            margin: EdgeInsets.all(5),
                            options: TreeNodeOptions(
                                child: Text(
                                  "${n.name}(${n.count})",
                                  style: TextStyle(color: Colors.white),
                                ),
                                onTap: () {},
                                color: Colors.primaries[
                                random.nextInt(Colors.primaries.length)]),
                            value: n.count,
                          ),
                    )
                        .toList())
              ],
            ),
          );
        },
        error: (c, e) {
          return const Text("error");
        },
        loading: () => CircularProgressIndicator());
  }
}
