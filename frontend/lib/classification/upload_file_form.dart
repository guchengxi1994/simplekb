import 'package:data_table_2/data_table_2.dart';
import 'package:dropdown_button2/dropdown_button2.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/api/models/file_upload_by_type_model.dart';
// ignore: depend_on_referenced_packages
import 'package:collection/collection.dart';
import 'package:frontend/classification/add_tag_button.dart';
import 'package:frontend/classification/upload_file_state.dart';

import 'upload_file_notifier.dart';

class UploadFileForm extends ConsumerStatefulWidget {
  const UploadFileForm({super.key, required this.type});
  final int type;

  @override
  ConsumerState<UploadFileForm> createState() => _UploadFileFormState();
}

class _UploadFileFormState extends ConsumerState<UploadFileForm> {
  @override
  Widget build(BuildContext context) {
    final state = ref.watch(uploadFileProvider);

    return Material(
      borderRadius: BorderRadius.circular(15),
      elevation: 10,
      child: Container(
        padding: EdgeInsets.all(20),
        decoration: BoxDecoration(borderRadius: BorderRadius.circular(15)),
        width: MediaQuery.of(context).size.width * 0.8,
        height: MediaQuery.of(context).size.height * 0.8,
        child: Column(
          children: [
            Row(
              children: [
                Expanded(
                    child: Container(
                  height: 30,
                  padding: EdgeInsets.only(left: 20, right: 20),
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(5),
                      border: Border.all(color: Colors.grey)),
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: Text(state.file?.name ?? ""),
                  ),
                )),
                SizedBox(width: 10),
                ElevatedButton(
                    style: ButtonStyle(
                      shape: WidgetStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(5.0), // 设置圆角半径
                        ),
                      ),
                      backgroundColor:
                          WidgetStateProperty.all<Color>(Colors.blue), // 设置背景颜色
                      padding: WidgetStateProperty.all<EdgeInsetsGeometry>(
                        EdgeInsets.symmetric(
                            horizontal: 8.0, vertical: 8.0), // 设置内边距
                      ),
                      textStyle: WidgetStateProperty.all<TextStyle>(
                        TextStyle(fontSize: 14, color: Colors.white), // 设置文本样式
                      ),
                    ),
                    onPressed: () {
                      ref
                          .read(uploadFileProvider.notifier)
                          .uploadFile(widget.type);
                    },
                    child: Text(
                      "文件选择",
                      style: TextStyle(color: Colors.white),
                    )),
                SizedBox(width: 10),
                ElevatedButton(
                    style: ButtonStyle(
                      shape: WidgetStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(5.0), // 设置圆角半径
                        ),
                      ),
                      backgroundColor:
                          WidgetStateProperty.all<Color>(Colors.blue), // 设置背景颜色
                      padding: WidgetStateProperty.all<EdgeInsetsGeometry>(
                        EdgeInsets.symmetric(
                            horizontal: 8.0, vertical: 8.0), // 设置内边距
                      ),
                      textStyle: WidgetStateProperty.all<TextStyle>(
                        TextStyle(fontSize: 14, color: Colors.white), // 设置文本样式
                      ),
                    ),
                    onPressed: state.data == null
                        ? null
                        : () {
                            ref
                                .read(uploadFileProvider.notifier)
                                .submitUpload();
                          },
                    child: state.isLoading
                        ? SizedBox.square(
                            dimension: 20,
                            child: CircularProgressIndicator(),
                          )
                        : Text(
                            "确认上传",
                            style: TextStyle(color: Colors.white),
                          ))
              ],
            ),
            SizedBox(
              height: 20,
            ),
            Expanded(child: buildDatatable(state))
          ],
        ),
      ),
    );
  }

  DataTable2 buildDatatable(UploadFileState state) {
    return DataTable2(
      border: TableBorder(
          top: const BorderSide(color: Colors.black),
          bottom: BorderSide(color: Colors.grey[300]!),
          left: BorderSide(color: Colors.grey[300]!),
          right: BorderSide(color: Colors.grey[300]!),
          verticalInside: BorderSide(color: Colors.grey[300]!),
          horizontalInside: const BorderSide(color: Colors.grey, width: 1)),
      columns: buildDataColumn(state),
      rows: state.data == null ? [] : [buildRow(state.data!)],
      columnSpacing: 12,
      horizontalMargin: 10,
    );
  }

  List<DataColumn2> buildDataColumn(UploadFileState state) {
    return [
      DataColumn2(label: Text("文件类型"), fixedWidth: 120),
      DataColumn2(
          label: DropdownButtonHideUnderline(
            child: DropdownButton2<String>(
              isExpanded: true,
              hint: Text(
                'Select Item',
                style: TextStyle(
                  fontSize: 14,
                  color: Theme.of(context).hintColor,
                ),
              ),
              items: chunkTypes
                  .map((String item) => DropdownMenuItem<String>(
                        value: item,
                        child: Text(
                          item,
                          style: const TextStyle(
                            fontSize: 14,
                          ),
                        ),
                      ))
                  .toList(),
              value: state.chunkType,
              onChanged: (String? value) {
                if (value == null) return;

                ref
                    .read(uploadFileProvider.notifier)
                    .changeChunkType(value, widget.type);
              },
              buttonStyleData: const ButtonStyleData(
                padding: EdgeInsets.symmetric(horizontal: 16),
                height: 40,
                width: 140,
              ),
              dropdownStyleData: DropdownStyleData(width: 140),
              menuItemStyleData: const MenuItemStyleData(
                height: 40,
              ),
            ),
          ),
          fixedWidth: 120),
      DataColumn2(label: Text("文件内容"), size: ColumnSize.L),
      DataColumn2(label: Text("关键字"), size: ColumnSize.L),
    ];
  }

  DataRow2 buildRow(FileUploadByTypeModel v) {
    return DataRow2(
      specificRowHeight: v.chunks.length * 80,
      cells: [
        DataCell(Text(v.typeName)),
        DataCell(Text(v.chunkType)),
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
                              child: Text(
                            c.content.replaceAll("\n", "🤏🏻"),
                            maxLines: 4,
                            softWrap: true,
                            overflow: TextOverflow.ellipsis,
                            style: TextStyle(color: Colors.blue),
                          ))
                        ],
                      ),
                    ))
                .toList(),
          ),
        )),
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
                            child: Wrap(
                          spacing: 10,
                          runSpacing: 10,
                          children: [
                            ...c.keywords.mapIndexed((kid, k) => Chip(
                                  onDeleted: () {
                                    ref
                                        .read(uploadFileProvider.notifier)
                                        .removeKeyword(c.keywords[kid], i);
                                  },
                                  deleteIcon: Icon(Icons.delete),
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(16.0),
                                    side: BorderSide(color: Colors.blue),
                                  ),
                                  label: Text(k),
                                  avatar: Text("🫶🏻"),
                                )),
                            AddTagButton(onSave: (s) {
                              ref
                                  .read(uploadFileProvider.notifier)
                                  .addKeyword(s, i);
                            }),
                          ],
                        ))
                      ],
                    )))
                .toList(),
          ),
        ))
      ],
    );
  }
}
