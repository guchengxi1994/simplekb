import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/classification/classification_state.dart';
import 'package:frontend/utils.dart';
import 'package:treemap/treemap.dart';

import 'classification_details_screen.dart';
import 'classification_notifier.dart';

class ClassificationScreen extends ConsumerWidget {
  const ClassificationScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(classificationProvider);

    return state.when(
        data: (v) {
          return buildNavigator(v, ref);
        },
        error: (c, e) {
          return Center(
            child: Text("error $e"),
          );
        },
        loading: () => Center(
              child: CircularProgressIndicator(),
            ));
  }

  Widget buildNavigator(ClassificationState v, WidgetRef ref) {
    return Navigator(
      initialRoute: "/",
      onGenerateRoute: (settings) {
        final arguments = settings.arguments;

        switch (settings.name) {
          case "/details":
            return MaterialPageRoute(
              builder: (context) => ClassificationDetailsScreen(
                arg: arguments as int,
              ),
            );
          default:
            return MaterialPageRoute(
              builder: (context) {
                return Container(
                  padding: EdgeInsets.all(20),
                  width: MediaQuery.of(context).size.width,
                  height: MediaQuery.of(context).size.height,
                  child: TreeMapLayout(
                    duration: Duration(milliseconds: 500),
                    tile: Squarify(),
                    children: [
                      TreeNode.node(
                          children: v.nodes
                              .map(
                                (n) => TreeNode.leaf(
                                  margin: EdgeInsets.all(5),
                                  options: TreeNodeOptions(
                                      child: Text(
                                        "${n.name}(${n.count})",
                                        style: TextStyle(color: Colors.white),
                                      ),
                                      onTap: () {
                                        if (n.count == 0) {
                                          ToastUtils.error(context,
                                              title: "无数据");
                                          return;
                                        }

                                        Navigator.pushNamed(context, '/details',
                                            arguments: n.id);
                                      },
                                      color: Colors.primaries[
                                          n.id % Colors.primaries.length]),
                                  value: n.count,
                                ),
                              )
                              .toList())
                    ],
                  ),
                );
              },
            );
        }
      },
    );
  }
}
