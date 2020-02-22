package cn.edu.hebut.easydesign.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextActivity;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.RichTextEditor.RichTextEditor;


/**
 * 这个是一个富文本编辑器使用的例子
 *
 */

public class RichEditorTest extends HoldContextActivity implements View.OnClickListener {
    private RichTextEditor richTextEditor;

    private Button superscriptButton;
    private Button subscriptButton;
    private Button strikethroughButton;
    private Button underlineButton;
    private Button backgroundColorButton;
    private Button foregroundColorButton;
    private Button hyperlinkButton;
    private Button fontSizeButton;
    private TextView testView;
    private int lastBackgroundColor = -1;
    private int lastForegroundColor = -1;
    private int lastFontSize = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_editor_test);

        richTextEditor = findViewById(R.id.RichTextEditor);
        setUpFunctions();

    }

    private void setUpFunctions() {
        superscriptButton = findViewById(R.id.Superscript);
        subscriptButton = findViewById(R.id.Subscript);
        strikethroughButton = findViewById(R.id.Strikethrough);
        underlineButton = findViewById(R.id.Underline);
        backgroundColorButton = findViewById(R.id.BackgroundColor);
        foregroundColorButton = findViewById(R.id.ForegroundColor);
        hyperlinkButton = findViewById(R.id.Hyperlink);
        fontSizeButton = findViewById(R.id.FontSized);
        testView = findViewById(R.id.TextViewForComplexString);
        testView.setBackgroundColor(getResources().getColor(R.color.category_radio_button_unselected));

        subscriptButton.setOnClickListener(this);
        superscriptButton.setOnClickListener(this);
        strikethroughButton.setOnClickListener(this);
        underlineButton.setOnClickListener(this);
        backgroundColorButton.setOnClickListener(this);
        foregroundColorButton.setOnClickListener(this);
        hyperlinkButton.setOnClickListener(this);
        fontSizeButton.setOnClickListener(this);
        testView.setOnClickListener(this);

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Subscript:
                Toast.makeText(this, "Subscript", Toast.LENGTH_LONG).show();
                richTextEditor.commonFontStyle(ComplexString.SUBSCRIPT,
                        !richTextEditor.containCommonStyle(ComplexString.SUBSCRIPT));
                break;
            case R.id.Superscript:
                richTextEditor.commonFontStyle(ComplexString.SUPERSCRIPT,
                        !richTextEditor.containCommonStyle(ComplexString.SUPERSCRIPT));
                break;
            case R.id.Underline:
                richTextEditor.commonFontStyle(ComplexString.UNDERLINE,
                        !richTextEditor.containCommonStyle(ComplexString.UNDERLINE));
                break;
            case R.id.Strikethrough:
                richTextEditor.commonFontStyle(ComplexString.STRIKE_THROUGH,
                        !richTextEditor.containCommonStyle(ComplexString.STRIKE_THROUGH));
                break;
            case R.id.BackgroundColor:
                showBackgroundColorMenu(v);
                break;
            case R.id.ForegroundColor:
                showForegroundColorMenu(v);
                break;
            case R.id.Hyperlink:
                showLinkDialog();
                break;
            case R.id.FontSized:
                showFontSizeMenu(v);
            case R.id.TextViewForComplexString:
                richTextEditor.getComplexString().SetToTextView(testView);
        }
    }

    private void showFontSizeMenu(View v) {
        PopupMenu fontSizeMenu = new PopupMenu(this, v);
        getMenuInflater().inflate(R.menu.font_size_menu, fontSizeMenu.getMenu());
        fontSizeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.RichTextEditorFontSmallSize:
                        if(lastFontSize == RichTextEditor.SMALL_FONT)
                            return true;
                        richTextEditor.fontSize(lastFontSize, false);
                        richTextEditor.fontSize(RichTextEditor.SMALL_FONT, true);
                        lastFontSize = RichTextEditor.SMALL_FONT;
                        return true;
                    case R.id.RichTextEditorFontNormalSize:
                        if(lastFontSize == RichTextEditor.NORMAL_FONT)
                            return true;
                        richTextEditor.fontSize(lastFontSize, false);
                        richTextEditor.fontSize(RichTextEditor.NORMAL_FONT, true);
                        lastFontSize = RichTextEditor.NORMAL_FONT;
                        return true;
                    case R.id.RichTextEditorHugeFontSize:
                        if(lastFontSize == RichTextEditor.HUGE_FONT)
                            return true;
                        richTextEditor.fontSize(lastFontSize, false);
                        richTextEditor.fontSize(RichTextEditor.HUGE_FONT, true);
                        lastFontSize = RichTextEditor.HUGE_FONT;
                        return true;
                    case R.id.RichTextEditorDefaultFontSize:
                        if(lastFontSize == -1)
                            return true;
                        richTextEditor.fontSize(lastFontSize, false);
                        lastFontSize = -1;
                        return true;
                    default:
                        return false;
                }
            }
        });
        fontSizeMenu.show();
    }

    private void showLinkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.rich_text_editor_hyperlink_dialog, null, false);
        final EditText editText = view.findViewById(R.id.RichTextEditorHyperLinkDialogInput);
        builder.setView(view);
        builder.setTitle("请输入网址");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }
                // When KnifeText lose focus, use this method
                richTextEditor.hyperlink(link);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showBackgroundColorMenu(View v) {
        PopupMenu backgroundColorPopupMenu = new PopupMenu(this, v);
        getMenuInflater().inflate(R.menu.background_color_menu, backgroundColorPopupMenu.getMenu());
        backgroundColorPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.background_color_menu_red:
                        if(lastBackgroundColor == RichTextEditor.TEXT_BACKGROUND_COLOR_RED)
                            return true;
                        richTextEditor.backgroundColor(lastBackgroundColor, false);
                        richTextEditor.backgroundColor(RichTextEditor.TEXT_BACKGROUND_COLOR_RED, true);
                        lastBackgroundColor = RichTextEditor.TEXT_BACKGROUND_COLOR_RED;
                        return true;
                    case R.id.background_color_menu_yellow:
                        if(lastBackgroundColor == RichTextEditor.TEXT_BACKGROUND_COLOR_YELLOW)
                            return true;
                        richTextEditor.backgroundColor(lastBackgroundColor, false);
                        richTextEditor.backgroundColor(RichTextEditor.TEXT_BACKGROUND_COLOR_YELLOW, true);
                        lastBackgroundColor = RichTextEditor.TEXT_BACKGROUND_COLOR_YELLOW;
                        return true;
                    case R.id.background_color_menu_blue:
                        if(lastBackgroundColor == RichTextEditor.TEXT_BACKGROUND_COLOR_BLUE)
                            return true;
                        richTextEditor.backgroundColor(lastBackgroundColor, false);
                        richTextEditor.backgroundColor(RichTextEditor.TEXT_BACKGROUND_COLOR_BLUE, true);
                        lastBackgroundColor = RichTextEditor.TEXT_BACKGROUND_COLOR_BLUE;
                        return true;
                    case R.id.background_color_menu_green:
                        if(lastBackgroundColor == RichTextEditor.TEXT_BACKGROUND_COLOR_GREEN)
                            return true;
                        richTextEditor.backgroundColor(lastBackgroundColor, false);
                        richTextEditor.backgroundColor(RichTextEditor.TEXT_BACKGROUND_COLOR_GREEN, true);
                        lastBackgroundColor = RichTextEditor.TEXT_BACKGROUND_COLOR_GREEN;
                        return true;
                    case R.id.background_color_menu_purple:
                        if(lastBackgroundColor == RichTextEditor.TEXT_BACKGROUND_COLOR_PURPLE)
                            return true;
                        richTextEditor.backgroundColor(lastBackgroundColor, false);
                        richTextEditor.backgroundColor(RichTextEditor.TEXT_BACKGROUND_COLOR_PURPLE, true);
                        lastBackgroundColor = RichTextEditor.TEXT_BACKGROUND_COLOR_PURPLE;
                        return true;
                    case R.id.background_color_menu_clear:
                        richTextEditor.backgroundColor(lastBackgroundColor, false);
                        lastBackgroundColor = -1;
                        return true;
                    default:
                        return false;
                }
            }
        });
        backgroundColorPopupMenu.show();
    }

    private void showForegroundColorMenu(View v) {
        PopupMenu foregroundColorPopupMenu = new PopupMenu(this, v);
        getMenuInflater().inflate(R.menu.foreground_color_menu, foregroundColorPopupMenu.getMenu());
        foregroundColorPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.foreground_color_menu_red:
                        if(lastForegroundColor == RichTextEditor.TEXT_FOREGROUND_COLOR_RED)
                            return true;
                        richTextEditor.foregroundColor(lastForegroundColor, false);
                        richTextEditor.foregroundColor(RichTextEditor.TEXT_FOREGROUND_COLOR_RED, true);
                        lastForegroundColor = RichTextEditor.TEXT_FOREGROUND_COLOR_RED;
                        return true;
                    case R.id.foreground_color_menu_yellow:
                        if(lastForegroundColor == RichTextEditor.TEXT_FOREGROUND_COLOR_YELLOW)
                            return true;
                        richTextEditor.foregroundColor(lastForegroundColor, false);
                        richTextEditor.foregroundColor(RichTextEditor.TEXT_FOREGROUND_COLOR_YELLOW, true);
                        lastForegroundColor = RichTextEditor.TEXT_FOREGROUND_COLOR_YELLOW;
                        return true;
                    case R.id.foreground_color_menu_blue:
                        if(lastForegroundColor == RichTextEditor.TEXT_FOREGROUND_COLOR_BLUE)
                            return true;
                        richTextEditor.foregroundColor(lastForegroundColor, false);
                        richTextEditor.foregroundColor(RichTextEditor.TEXT_FOREGROUND_COLOR_BLUE, true);
                        lastForegroundColor = RichTextEditor.TEXT_FOREGROUND_COLOR_BLUE;
                        return true;
                    case R.id.foreground_color_menu_green:
                        if(lastForegroundColor == RichTextEditor.TEXT_FOREGROUND_COLOR_GREEN)
                            return true;
                        richTextEditor.foregroundColor(lastForegroundColor, false);
                        richTextEditor.foregroundColor(RichTextEditor.TEXT_FOREGROUND_COLOR_GREEN, true);
                        lastForegroundColor = RichTextEditor.TEXT_FOREGROUND_COLOR_GREEN;
                        return true;
                    case R.id.foreground_color_menu_purple:
                        if(lastForegroundColor == RichTextEditor.TEXT_FOREGROUND_COLOR_PURPLE)
                            return true;
                        richTextEditor.foregroundColor(lastForegroundColor, false);
                        richTextEditor.foregroundColor(RichTextEditor.TEXT_FOREGROUND_COLOR_PURPLE, true);
                        lastForegroundColor = RichTextEditor.TEXT_FOREGROUND_COLOR_PURPLE;
                        return true;
                    case R.id.foreground_color_menu_clear:
                        if(lastForegroundColor == -1)
                            return true;
                        richTextEditor.foregroundColor(lastForegroundColor, false);
                        lastForegroundColor = -1;
                        return true;
                    default:
                        return false;
                }
            }
        });
        foregroundColorPopupMenu.show();
    }
}
