package com.oznurdemir.memberregistrationlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MembersListActivity extends AppCompatActivity {
    ListView listView;// ListView nesnesi tanımlama
    ArrayList<String> titles = new ArrayList<String>(); // Başlıkların tutulacağı ArrayList
    ArrayAdapter arrayAdapter;// ArrayAdapter nesnesi tanımlama


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);// activity_members_list.xml layout'unu belirleme

        listView = findViewById(R.id.listView);// ListView'i belirleme

        SQLiteDatabase database = openOrCreateDatabase("Members",MODE_PRIVATE,null);// "Members" adında bir veritabanı açma veya oluşturma

        Cursor cursor = database.rawQuery("select * from members",null); // members tablosundaki tüm verileri seçmek için bir Cursor oluşturma
        int id = cursor.getColumnIndex("id");// id sütununun indeksini alıyoruz
        int name = cursor.getColumnIndex("name");// name sütununun indeksini alıyoruz
        int surname = cursor.getColumnIndex("surname");// surname sütununun indeksini alıyoruz
        int age = cursor.getColumnIndex("age");// age sütununun indeksini alıyoruz
        int sport = cursor.getColumnIndex("sport");// sport sütununun indeksini alıyoruz


        titles.clear();// Başlıkları temizleme

        arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,titles);
        // ArrayAdapter oluşturma ve bağlı olduğu context'i, düzeni ve verileri belirleme
        listView.setAdapter(arrayAdapter);// ListView'e ArrayAdapter'i bağlama

        ArrayList<Members> members = new ArrayList<Members>();// Members sınıfından bir ArrayList oluşturma
        /*
        Bu ArrayList, veritabanından çekilen her bir üye verisinin detaylarını tutar. Her döngü iterasyonunda, bir Members nesnesi oluşturulur ve
        bu nesnenin alanlarına ilgili veriler atanır. Daha sonra oluşturulan Members nesnesi members ArrayList'ine eklenir.
        Bu members ArrayList'i, ListView'da görüntülenmek üzere verilerin tutulduğu bir veri yapısıdır.
        ListView'da her bir üye verisi için bir satır görüntülenirken, bu verileri temsil eden Members nesneleri members ArrayList'inde saklanır.
        Bu sayede, veritabanındaki üye verilerini daha sonra kullanmak veya işlemek (silme,güncelleme) için members ArrayList'ini kullanabilirsiniz.
         */

        if(cursor.moveToNext()){// Cursor bir sonraki satıra geçebilirse (veri varsa)
            do{
                Members m = new Members();// Yeni bir Members nesnesi oluşturma
                m.id = cursor.getString(id);// Members nesnesine id değerini atama
                m.name = cursor.getString(name);// Members nesnesine name değerini atama
                m.surname = cursor.getString(surname);// Members nesnesine surname değerini atama
                m.age = cursor.getString(age); // Members nesnesine age değerini atama
                m.sport = cursor.getString(sport);// Members nesnesine sport değerini atama

                members.add(m);// Members nesnesini members ArrayList'ine ekleme

                titles.add(cursor.getString(id) + "   " + cursor.getString(name) + " " + cursor.getString(surname) + " ( Yaş: " + cursor.getString(age) + ", Spor: " + cursor.getString(sport)+")");
                // Başlıklar listesine id, name ve surname değerlerini ekleyerek satırı oluşturma


            }while(cursor.moveToNext()); // Bir sonraki satıra geçebilirse döngüyü devam ettir
            arrayAdapter.notifyDataSetChanged();// ArrayAdapter'i güncelleme
            listView.invalidateViews();// ListView'i yeniden çizme

        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Uzun tıklama olayı dinleyicisi
                AlertDialog.Builder alert =new AlertDialog.Builder(MembersListActivity.this);
                // Alert dialog oluşturma
                alert.setTitle("Yapılacak İşlem");
                alert.setMessage("Yapmak İstediğiniz İşlemi Seçiniz.");
                alert.setCancelable(false);
                alert.setNeutralButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Güncelle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Güncelleme işlemi
                        Members m = members.get(position);// Seçilen üyenin bilgilerini al
                        Intent intent = new Intent(getApplicationContext(),EditMemberActivity.class);
                        intent.putExtra("id",m.id);// id'yi intent'e ekle
                        intent.putExtra("name",m.name);// name'i intent'e ekle
                        intent.putExtra("surname",m.surname);// surname'i intent'e ekle
                        intent.putExtra("age",m.age); // age'i intent'e ekle
                        intent.putExtra("sport",m.sport);// sport'u intent'e ekle
                        startActivity(intent);// EditMemberActivity'e geçiş yap
                    }
                });
                alert.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Silme işlemi
                        try {
                            Members m = members.get(position);// Seçilen üyenin bilgilerini al
                            // String id = idText.getText().toString();
                            SQLiteDatabase database = openOrCreateDatabase("Members",MODE_PRIVATE,null);

                            String sql = "delete from members where id = ?";
                            SQLiteStatement statement = database.compileStatement(sql);

                            statement.bindString(1, m.id);// Sileceğimiz üyenin id'sini bağla
                            statement.execute();// SQL ifadesini çalıştır
                            Toast.makeText(MembersListActivity.this, "Üye Silindi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MembersListActivity.this,MembersListActivity.class);
                            startActivity(intent); // MembersListActivity'yi yeniden başlat


                        }catch (Exception e){
                            //Herhangi bir hata durumunda hatayı yakalar ve kullanıcıya hata mesajını gösterir
                            Toast.makeText(MembersListActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.show();
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add){

            Intent intent = new Intent(MembersListActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}