package com.x;

import com.intellij.codeInsight.editorActions.PasteHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessagesService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.rpc.CommandProcessor;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class delBlankLine extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        SelectionModel selectionModel = editor.getSelectionModel();
        Document document = editor.getDocument();
        LogicalPosition logicalPosition = caretModel.getLogicalPosition(); //不带自动换行
        VisualPosition visualPosition = caretModel.getVisualPosition(); //带自动换行产生的行
        String codeText = document.getText().toString();
        String[] stringList = codeText.split("\n");
//      +"\n"+String.valueOf(logicalPosition.column)+"\n"+String.valueOf(logicalPosition.line)
        String lineText = stringList[logicalPosition.line];
        String lineTexts = lineText.replaceAll(" ", "");
        if (lineTexts.equals("")) {
             selectionModel.selectLineAtCaret(); //设置当前行选中
//            EditorActionManager actionManager = EditorActionManager.getInstance();
//            //Insert one more caret below the active caret
//            EditorActionHandler actionHandler = actionManager.getActionHandler(IdeActions.ACTION_EDITOR_DELETE_LINE);
//            actionHandler.execute(editor, caretModel.getCurrentCaret(), e.getDataContext());
            String[] d_arr = delete(logicalPosition.line, stringList);
            final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
            //Access document, caret, and selection
            final int start = selectionModel.getSelectionStart();
            final int end = selectionModel.getSelectionEnd();
            //New instance of Runnable to make a replacement
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    document.setText(arrToString(d_arr).toString());
                }
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
            selectionModel.removeSelection();
//            Messages.showMessageDialog(arrToString(stringList).toString(), "info", Messages.getInformationIcon());
        }
//        Messages.showMessageDialog(stringList[logicalPosition.line].toString(), "info", Messages.getInformationIcon());
    }

    public String[] delete(int index, String array[]) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        list.remove(index);
        String[] newStr = list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组

        return newStr;
    }

    public String arrToString(String array[]) {
        StringBuffer s_b = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            s_b.append(array[i].toString() + "\n");
        }
        return s_b.toString();
//        return  StringUtils.join(array, "\n");
    }
}
