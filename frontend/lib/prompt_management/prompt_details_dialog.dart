import 'package:flutter/material.dart';
import 'package:markdown_widget/markdown_widget.dart';

class PromptDetailsDialog extends StatelessWidget {
  const PromptDetailsDialog(
      {super.key, required this.title, required this.content});
  final String title;
  final String content;

  @override
  Widget build(BuildContext context) {
    return Material(
      elevation: 10,
      borderRadius: BorderRadius.circular(20),
      child: Container(
        padding: EdgeInsets.all(20),
        height: MediaQuery.of(context).size.height * 0.8,
        width: MediaQuery.of(context).size.width * 0.8,
        child: Column(
          children: [
            SizedBox(
              height: 30,
              child: Row(
                children: [
                  Spacer(),
                  InkWell(
                    onTap: () {
                      Navigator.pop(context);
                    },
                    child: Icon(Icons.close),
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 40,
              child: Center(
                  child: Text(
                title,
                style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
              )),
            ),
            SizedBox(
              height: 20,
            ),
            Expanded(
              child: MarkdownWidget(data: content),
            )
          ],
        ),
      ),
    );
  }
}
