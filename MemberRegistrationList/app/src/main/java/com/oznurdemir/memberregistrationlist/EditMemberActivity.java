package com.oznurdemir.memberregistrationlist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditMemberActivity extends AppCompatActivity {
    EditText nameText,surnameText,ageText,sportText;
    Button update;
    String id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        nameText = findViewById(R.id.isimEditText);
        surnameText = findViewById(R.id.soyisimEditText);
        ageText = findViewById(R.id.yasEditText);
        sportText = findViewById(R.id.sporDaliEditText);


        update = findViewById(R.id.updateButton);


        Intent intent = getIntent();
        String ed1 = intent.getStringExtra("id").toString();
        String ed2 = intent.getStringExtra("name").toString();
        String ed3 = intent.getStringExtra("surname").toString();
        String ed4 = intent.getStringExtra("age").toString();
        String ed5 = intent.getStringExtra("sport").toString();
        id = ed1;

        // EditText alanlarına intent ile gelen verileri yerleştiriyoruz
        nameText.setText(ed2);
        surnameText.setText(ed3);
        ageText.setText(ed4);
        sportText.setText(ed5);
//        idText.setText(ed1);





        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }





    public void update(){
        //işlemlerimizi try ve catch içinde yapacağız.SQL komutlarında hata alırsak söylenmeme ihtimali ve yanlış yazma ihtimalimiz için

        try {
            //Kullanıcı tarafından girilen metinleri ilgili değişkenlere atarız
            String name = nameText.getText().toString();
            String surname = surnameText.getText().toString();
            String age = ageText.getText().toString();
            String sport = sportText.getText().toString();
            //String id = idText.getText().toString();

            // Members isminde veritabanı yoksa oluştur varsa aç.
            SQLiteDatabase database = openOrCreateDatabase("Members",MODE_PRIVATE,null);


            String sql = "update members set name = ?,surname = ?,age = ?,sport = ? where id = ?";
            //Bu satırda, SQL ifadesi sql kullanılarak bir SQLiteStatement nesnesi oluşturuluyor.
            // Bu nesne, SQL komutunu bağlayacağımız parametreleri eklememizi sağlar.
            SQLiteStatement statement = database.compileStatement(sql);

            // parametreleri bağlıyoruz
            statement.bindString(1,name);
            statement.bindString(2,surname);
            statement.bindString(3,age);
            statement.bindString(4,sport);
            statement.bindString(5, id);
            statement.execute();
            Toast.makeText(this, "Üye Güncellendi", Toast.LENGTH_SHORT).show();

            // MembersListActivity'ye geri dönüş yapılıyor
            Intent intent1 = new Intent(getApplicationContext(),MembersListActivity.class);
            startActivity(intent1);




        }catch (Exception e){
            //Herhangi bir hata durumunda hatayı yakalar ve kullanıcıya hata mesajını gösterir
            Toast.makeText(this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}