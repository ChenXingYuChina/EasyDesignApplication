package cn.edu.hebut.easydesign.Activity.SignUp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.R;

public class SignUpBase extends HoldContextAppCompatActivity implements View.OnClickListener {
    private EditText userName, password, passwordCopy, email;
    private Spinner identity;
    private TextView pass, nextLabel;
    private ImageView toDetail;

    private TextField[] textFields = new TextField[5];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_base_layout);
        userName = findViewById(R.id.user_name_input);
        password = findViewById(R.id.password);
        passwordCopy = findViewById(R.id.password_copy);
        email = findViewById(R.id.email_input);
        toDetail = findViewById(R.id.to_detail);
        identity = findViewById(R.id.identity_select);
        pass = findViewById(R.id.pass_sign_up_detail);
        nextLabel = findViewById(R.id.to_detail_label);
    }

    @Override
    public void onClick(View v) {

    }

    private int loadData() {
        String name = userName.getText().toString();
        String password = this.password.getText().toString();
        String email = this.email.getText().toString();
        return 0;
    }
}
