import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'chat_response.dart';
import 'llm_response_messagebox.dart';
import 'messagebox.dart';
import 'messagebox_state.dart';

class MessageNotifier extends AutoDisposeNotifier<MessageState> {
  final ScrollController scrollController = ScrollController();

  @override
  MessageState build() {
    return MessageState(isKnowledgeBaseChat: true);
  }

  changeKnowledgeBaseChat(bool b) {
    if (state.isKnowledgeBaseChat != b) {
      state = MessageState(isKnowledgeBaseChat: b);
    }
  }

  addMessageBox(MessageBox box) {
    if (state.isLoading) {
      return;
    }

    final l = List<MessageBox>.from(state.messageBox)
      ..add(box);

    state = MessageState(
        messageBox: l,
        isLoading: state.isLoading,
        isKnowledgeBaseChat: state.isKnowledgeBaseChat);

    scrollController.jumpTo(
      scrollController.position.maxScrollExtent,
    );
  }

  jumpToMax() {
    scrollController.jumpTo(
      scrollController.position.maxScrollExtent,
    );
  }

  updateMessageBox(ChatResponse response) {
    final box = state.messageBox
        .where((element) =>
    element is ResponseMessageBox && element.id == response.uuid)
        .firstOrNull;

    if (box != null) {
      final l = List<MessageBox>.from(state.messageBox)
        ..remove(box);
      box.content += response.content ?? "";
      if (box is ResponseMessageBox) {
        box.stage = response.stage ?? "";
      }
      state = MessageState(
          messageBox: l..add(box),
          isLoading: state.isLoading,
          isKnowledgeBaseChat: state.isKnowledgeBaseChat);
    } else {
      final l = List<MessageBox>.from(state.messageBox)
        ..add(ResponseMessageBox(
            content: response.content ?? "",
            id: response.uuid!,
            stage: response.stage ?? ""));
      state = MessageState(
          isLoading: state.isLoading,
          messageBox: l,
          isKnowledgeBaseChat: state.isKnowledgeBaseChat);
    }

    scrollController.jumpTo(
      scrollController.position.maxScrollExtent,
    );
  }

  setLoading(bool b) {
    if (b != state.isLoading) {
      state = MessageState(
          messageBox: state.messageBox,
          isLoading: b,
          isKnowledgeBaseChat: state.isKnowledgeBaseChat);
    }
  }

// refresh(List<HistoryMessages> messages) {
//   if (state.isLoading) {
//     return;
//   }

//   List<MessageBox> boxes = [];
//   for (final i in messages) {
//     if (i.messageType == MessageType.query) {
//       boxes.add(RequestMessageBox(content: i.content ?? ""));
//     } else {
//       boxes.add(ResponseMessageBox(
//           content: i.content ?? "", id: "history_${i.id}"));
//     }
//   }

//   state = MessageState(
//       messageBox: boxes,
//       isLoading: false,
//       isKnowledgeBaseChat: state.isKnowledgeBaseChat);
// }
}

final messageProvider =
AutoDisposeNotifierProvider<MessageNotifier, MessageState>(
      () => MessageNotifier(),
);
