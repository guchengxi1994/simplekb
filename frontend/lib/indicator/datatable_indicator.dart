import 'package:flutter/material.dart';
// ignore: depend_on_referenced_packages
import 'package:collection/collection.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/classification/classification_details_notifier.dart';

typedef IndicatorBuilder = Widget Function(Widget child);

const double size = 30;

// ignore: must_be_immutable
class DatatableIndicator extends ConsumerWidget {
  DatatableIndicator({super.key, this.indicatorBuilder, required this.arg});
  final IndicatorBuilder? indicatorBuilder;
  final int arg;

  // ignore: prefer_final_fields
  late List<dynamic> _list = []; // 存放页码的数组

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(classificationDetailsProvider(arg)).value!.pageId;
    _list = ["上一页", state, "下一页"];

    return Wrap(
      spacing: 5,
      children: _list.mapIndexed((index, element) {
        if (index == 0) {
          return GestureDetector(
            onTap: () {
              ref.read(classificationDetailsProvider(arg).notifier).prevPage();
            },
            child: Container(
              width: size,
              height: size,
              decoration: BoxDecoration(
                  border: Border.all(
                      color: const Color.fromARGB(255, 230, 223, 223)),
                  borderRadius: const BorderRadius.all(Radius.circular(4)),
                  color: Colors.white),
              child: const Icon(Icons.chevron_left),
            ),
          );
        }
        if (index == _list.length - 1) {
          return GestureDetector(
              onTap: () {
                ref
                    .read(classificationDetailsProvider(arg).notifier)
                    .nextPage();
              },
              child: Container(
                width: size,
                height: size,
                decoration: BoxDecoration(
                    border: Border.all(
                        color: const Color.fromARGB(255, 230, 223, 223)),
                    borderRadius: const BorderRadius.all(Radius.circular(4)),
                    color: Colors.white),
                child: const Icon(Icons.chevron_right),
              ));
        }
        return Container(
          width: size,
          height: size,
          decoration: BoxDecoration(
              border:
                  Border.all(color: const Color.fromARGB(255, 230, 223, 223)),
              borderRadius: const BorderRadius.all(Radius.circular(4)),
              color: Colors.blue),
          child: Center(
            child: Text(state.toString()),
          ),
        );
      }).toList(),
    );
  }
}
