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

public class MainActivity extends AppCompatActivity {
    EditText nameText,surnameText,ageText,sportText;
    Button add,list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // XML dosyasındaki EditText bileşenlerini ilgili değişkenlere atıyoruz
        nameText = findViewById(R.id.isimEditText);
        surnameText = findViewById(R.id.soyisimEditText);
        ageText = findViewById(R.id.yasEditText);
        sportText = findViewById(R.id.sporDaliEditText);

        // XML dosyasındaki Button bileşenlerini ilgili değişkenlere atıyoruz
        add = findViewById(R.id.addButton);
        list = findViewById(R.id.listButton);

        // Listele düğmesine tıklama olayı atanıyor
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MembersListActivity'ye geçiş yapmak için intent oluşturuluyor ve başlatılıyor
                Intent intent = new Intent(getApplicationContext(),MembersListActivity.class);
                startActivity(intent);
            }
        });

        // Ekle düğmesine tıklama olayı atanıyor
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    public void insert(){
        //işlemlerimizi try ve catch içinde yapacağız.SQL komutlarında hata alırsak söylenmeme ihtimali ve yanlış yazma ihtimalimiz için

        try {
            //Kullanıcı tarafından girilen metinleri ilgili değişkenlere atarız
            String name = nameText.getText().toString();
            String surname = surnameText.getText().toString();
            String age = ageText.getText().toString();
            String sport = sportText.getText().toString();

            // Members isminde veritabanı yoksa oluştur varsa aç.
            SQLiteDatabase database = openOrCreateDatabase("Members",MODE_PRIVATE,null);

            // Veritabanında tablo oluşturuyoruz.
            database.execSQL("CREATE TABLE IF NOT EXISTS members (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, surname VARCHAR, age INT, sport VARCHAR)");


            //SQL ifadesi oluşturup veri ekleriz
            /*
            Bu adımlarda, SQL ifadesi oluşturulur ve SQLiteStatement kullanarak derlenir. '?' sembolleri,
            sonradan bağlanacak parametreleri temsil eder. Sonrasında bindString() ve bindLong() yöntemleri ile '?' sembollerine değerler atanır.
            Son olarak, execute() yöntemi ile SQL ifadesi çalıştırılır ve veri tabanına yeni bir kayıt eklenir.
             */
            String sql = "insert into members(name,surname,age,sport)values(?,?,?,?)";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.bindString(1,name);
            statement.bindString(2,surname);
            statement.bindString(3,age);
            statement.bindString(4,sport);
            statement.execute();
            Toast.makeText(this, "Üye Kaydı Başarılı", Toast.LENGTH_SHORT).show();

            // Sayfaya geri dönüldüğünde TextViewlar temiz olsun diye
            nameText.setText("");
            surnameText.setText("");
            ageText.setText("");
            sportText.setText("");
            nameText.requestFocus();


        }catch (Exception e){
            //Herhangi bir hata durumunda hatayı yakalar ve kullanıcıya hata mesajını gösterir
            Toast.makeText(this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}