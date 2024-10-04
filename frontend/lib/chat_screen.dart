import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/messagebox/chat_response.dart';

import 'input/input_field.dart';
import 'messagebox/error_messagebox.dart';
import 'messagebox/llm_request_messagebox.dart';
import 'messagebox/message_notifier.dart';
import 'messagebox/messagebox_state.dart';
import 'dart:convert'; // for jsonDecode
// ignore: avoid_web_libraries_in_flutter
import 'dart:html'; // 引入 dart:html 库

const api = "http://localhost:8080/kb/llm/chat";

class ChatScreen extends ConsumerStatefulWidget {
  const ChatScreen({super.key});

  @override
  ConsumerState<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends ConsumerState<ChatScreen> {
  @override
  Widget build(BuildContext context) {
    final state = ref.watch(messageProvider);

    return Column(
      children: [
        Flexible(
            child: SizedBox.expand(
          child: SingleChildScrollView(
            controller: ref.read(messageProvider.notifier).scrollController,
            padding: const EdgeInsets.only(left: 20, right: 20),
            child: Column(
              children: state.messageBox.map((e) => e.toWidget()).toList(),
            ),
          ),
        )),
        InputField(onSubmit: (s) => _handleInputMessage(s, state))
      ],
    );
  }

  _handleInputMessage(String s, MessageState state) async {
    // print(s);
    if (state.isLoading) {
      return;
    }

    ref
        .read(messageProvider.notifier)
        .addMessageBox(RequestMessageBox(content: s));

    // 创建 HttpRequest 对象
    HttpRequest request = HttpRequest();
    String alreadyReceived = '';

    // 发送 POST 请求
    request
      ..open('POST', api)
      ..setRequestHeader('Content-Type', 'application/json')
      ..onProgress.listen((event) {
        if (request.responseText != null) {
          var res = request.responseText!.replaceAll(alreadyReceived, '');
          alreadyReceived = request.responseText!;
          List<String> parts = res.split('\n');
          for (var part in parts) {
            if (part.startsWith('data:')) {
              // 提取 JSON 字符串
              String jsonString = part.substring(5).trimLeft(); // 去掉 "data:"
              if (jsonString.isNotEmpty) {
                // 尝试解析 JSON
                try {
                  Map<String, dynamic> jsonData = jsonDecode(jsonString);
                  ChatResponse chatResponse = ChatResponse.fromJson(jsonData);
                  ref
                      .read(messageProvider.notifier)
                      .updateMessageBox(chatResponse);
                } catch (_) {}
              }
            }
          }
        }
      })
      ..onError.listen((err) {
        ref
            .read(messageProvider.notifier)
            .addMessageBox(ErrorMessageBox(content: "异常"));
      })
      ..onLoadEnd.listen((fur) {
        /// TODO 处理所有收到的数据
        /// 因为上面的数据有的时候
        /// 会有处理异常
        print(request.responseText);
      })
      ..send(jsonEncode(<String, String>{
        'question': s,
      })); // 发送请求体

    ref.read(messageProvider.notifier).jumpToMax();
  }
}
