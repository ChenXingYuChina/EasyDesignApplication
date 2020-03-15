package cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor;

public interface SpecialErrorHandler {
    String HandleError(int code);

    String HandleError(Exception e);
}
