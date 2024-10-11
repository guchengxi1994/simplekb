import 'package:data_table_2/data_table_2.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/models/file_model.dart';
import 'package:frontend/indicator/datatable_indicator.dart';
// ignore: depend_on_referenced_packages
import 'package:collection/collection.dart';

import 'classification_details_notifier.dart';

class ClassificationDetailsScreen extends ConsumerWidget {
  const ClassificationDetailsScreen({super.key, required this.arg});
  final int arg;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(classificationDetailsProvider(arg));

    return state.when(
      data: (v) {
        return Scaffold(
          appBar: AppBar(
            elevation: 0,
            backgroundColor: Colors.transparent,
            automaticallyImplyLeading: false,
            leading: InkWell(
              onTap: () {
                Navigator.pop(context);
              },
              child: Icon(Icons.arrow_back),
            ),
            title: Row(
              children: [
                Text(
                  v.fileList.files.first.typeName,
                  style: TextStyle(fontSize: 20),
                )
              ],
            ),
          ),
          body: Column(
            children: [
              Expanded(
                  child: Padding(
                padding: EdgeInsets.all(20),
                child: buildDataTable(v.fileList),
              )),
              Center(
                child: DatatableIndicator(
                  arg: arg,
                ),
              ),
              SizedBox(
                height: 20,
              ),
            ],
          ),
        );
      },
      error: (error, stackTrace) {
        return Center(
          child: Text("error $error"),
        );
      },
      loading: () => Center(
        child: CircularProgressIndicator(),
      ),
    );
  }

  DataTable2 buildDataTable(FileList v) {
    return DataTable2(
      border: TableBorder(
          top: const BorderSide(color: Colors.black),
          bottom: BorderSide(color: Colors.grey[300]!),
          left: BorderSide(color: Colors.grey[300]!),
          right: BorderSide(color: Colors.grey[300]!),
          verticalInside: BorderSide(color: Colors.grey[300]!),
          horizontalInside: const BorderSide(color: Colors.grey, width: 1)),
      columns: buildDataColumn(),
      rows: v.files.map((l) => buildRow(l)).toList(),
      columnSpacing: 12,
      horizontalMargin: 10,
    );
  }

  List<DataColumn2> buildDataColumn() {
    return [
      DataColumn2(label: Text("id"), fixedWidth: 80),
      DataColumn2(label: Text("文件名"), fixedWidth: 120),
      DataColumn2(label: Text("类型"), fixedWidth: 80),
      DataColumn2(label: Text("切片与关键字"), size: ColumnSize.L)
    ];
  }

  DataRow2 buildRow(File v) {
    return DataRow2(
      specificRowHeight: v.chunks.length * 80,
      cells: [
        DataCell(Text(v.id.toString())),
        DataCell(Text(v.name)),
        DataCell(Text(v.typeName)),
        DataCell(SizedBox(
          height: v.chunks.length * 80,
          child: Column(
            children: v.chunks
                .mapIndexed((i, c) => Container(
                      height: 80,
                      decoration: BoxDecoration(
                          border: (i != v.chunks.length - 1)
                              ? Border(bottom: BorderSide(color: Colors.blue))
                              : null),
                      child: Row(
                        children: [
                          Expanded(
                              flex: 7,
                              child: Text(
                                c.content.replaceAll("\n", "🤏🏻"),
                                maxLines: 1,
                                softWrap: true,
                                overflow: TextOverflow.ellipsis,
                                style: TextStyle(color: Colors.blue),
                              )),
                          VerticalDivider(
                            color: Colors.blue,
                          ),
                          Expanded(
                              flex: 3,
                              child: Wrap(
                                spacing: 10,
                                runSpacing: 10,
                                children: c.keywords
                                    .map((k) => Chip(
                                          shape: RoundedRectangleBorder(
                                            borderRadius:
                                                BorderRadius.circular(16.0),
                                            side:
                                                BorderSide(color: Colors.blue),
                                          ),
                                          label: Text(k),
                                          avatar: Text("🫶🏻"),
                                        ))
                                    .toList(),
                              ))
                        ],
                      ),
                    ))
                .toList(),
          ),
        )),
      ],
    );
  }
}
