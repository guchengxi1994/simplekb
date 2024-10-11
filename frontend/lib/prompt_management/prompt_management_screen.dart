import 'package:data_table_2/data_table_2.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/models/prompt_model.dart';
import 'package:frontend/prompt_management/prompt_details_dialog.dart';
import 'package:frontend/prompt_management/prompt_management_state.dart';

import 'prompt_management_notifer.dart';

class PromptManagementScreen extends ConsumerWidget {
  const PromptManagementScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(promptManagementProvider);

    return Scaffold(
      body: Builder(builder: (c) {
        return state.when(data: (v) {
          return Column(
            children: [
              Expanded(
                  child: Padding(
                padding: EdgeInsets.all(20),
                child: buildDataTable(v, c),
              )),
            ],
          );
        }, error: (error, stackTrace) {
          return Center(
            child: Text("error $error"),
          );
        }, loading: () {
          return Center(
            child: CircularProgressIndicator(),
          );
        });
      }),
    );
  }

  List<DataColumn2> buildDataColumn() {
    return [
      DataColumn2(label: Text("id"), fixedWidth: 80),
      DataColumn2(label: Text("名称"), fixedWidth: 120),
      DataColumn2(label: Text("内容"), size: ColumnSize.L)
    ];
  }

  DataTable2 buildDataTable(PromptManagementState v, BuildContext ctx) {
    return DataTable2(
      border: TableBorder(
          top: const BorderSide(color: Colors.black),
          bottom: BorderSide(color: Colors.grey[300]!),
          left: BorderSide(color: Colors.grey[300]!),
          right: BorderSide(color: Colors.grey[300]!),
          verticalInside: BorderSide(color: Colors.grey[300]!),
          horizontalInside: const BorderSide(color: Colors.grey, width: 1)),
      columns: buildDataColumn(),
      rows: v.promptList.map((l) => buildRow(l, ctx)).toList(),
      columnSpacing: 12,
      horizontalMargin: 10,
    );
  }

  DataRow2 buildRow(PromptModel v, BuildContext ctx) {
    return DataRow2(
      cells: [
        DataCell(Text(v.promptId.toString())),
        DataCell(Text(v.promptName)),
        DataCell(MouseRegion(
          cursor: SystemMouseCursors.click,
          child: GestureDetector(
            onDoubleTap: () {
              showGeneralDialog(
                  barrierColor: Colors.transparent,
                  barrierDismissible: true,
                  barrierLabel: 'prompt-${v.promptId}',
                  context: ctx,
                  pageBuilder: (c, _, __) {
                    return Center(
                      child: PromptDetailsDialog(
                          title: v.promptName, content: v.promptContent),
                    );
                  });
            },
            child: Text(v.promptContent.replaceAll("\n", "🤏🏻")),
          ),
        )),
      ],
    );
  }
}
