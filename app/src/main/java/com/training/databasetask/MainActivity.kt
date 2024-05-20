package com.training.databasetask

import android.R
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.training.databasetask.database.DataBaseHelper
import com.training.databasetask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dbHelper: DataBaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DataBaseHelper(this)

        binding.addButton.setOnClickListener { addContact() }
        binding.updateButton.setOnClickListener { updateContact() }
        binding.deleteButton.setOnClickListener { deleteContact() }
        binding.viewButton.setOnClickListener { viewContacts() }


    }


    private fun viewContacts() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(DataBaseHelper.COLUMN_ID, DataBaseHelper.COLUMN_NAME, DataBaseHelper.COLUMN_PHONE)
        val cursor = db.query(
            DataBaseHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val contacts = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DataBaseHelper.COLUMN_NAME))
                val phone = getString(getColumnIndexOrThrow(DataBaseHelper.COLUMN_PHONE))
                contacts.add("Name: $name, Phone: $phone")
            }
        }
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, contacts)
        binding.contactsListView.adapter = adapter
        clearFields()
    }

    private fun deleteContact() {
        val name = binding.nameEditText.text.toString()
        if (name.isNotEmpty()) {
            val db = dbHelper.writableDatabase
            val selection = "${DataBaseHelper.COLUMN_NAME} LIKE ?"
            val selectionArgs = arrayOf(name)
            db.delete(DataBaseHelper.TABLE_NAME, selection, selectionArgs)
            Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
        }
        clearFields()
    }

    private fun updateContact() {
        val name = binding.nameEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        if (name.isNotEmpty() && phone.isNotEmpty()) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DataBaseHelper.COLUMN_PHONE, phone)
            }
            val selection = "${DataBaseHelper.COLUMN_NAME} LIKE ?"
            val selectionArgs = arrayOf(name)
            db.update(DataBaseHelper.TABLE_NAME, values, selection, selectionArgs)
            Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Please enter name and phone number", Toast.LENGTH_SHORT).show()
        }
        clearFields()
    }

    private fun addContact() {
        if(binding.nameEditText.text.isNotEmpty() && binding.phoneEditText.text.isNotEmpty()){
            val db = dbHelper.writableDatabase
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val values = ContentValues().apply {
                put(DataBaseHelper.COLUMN_NAME, name)
                put(DataBaseHelper.COLUMN_PHONE, phone)
            }
            db.insert(DataBaseHelper.TABLE_NAME, null, values)
            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()

        }
        else{
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        clearFields()
    }
    private fun clearFields() {
        binding.nameEditText.text.clear()
        binding.phoneEditText.text.clear()
    }
}