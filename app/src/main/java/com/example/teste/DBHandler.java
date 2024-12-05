package com.example.teste;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHandler extends SQLiteOpenHelper {

    public static class User {
        private String name;
        private String email;
        private String password;
        private String morada;
        private String phone;

        // Construtor
        public User(String name, String email, String morada, String phone){
            this.name = name;
            this.email = email;
            this.morada = morada;
            this.phone = phone;

        }

        // Getters e Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public String getMorada() {
            return morada;
        }
        public void setMorada(String morada) {
            this.morada = morada;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    // Database name and version
    private static final String DB_NAME = "bitebalance";
    private static final int DB_VERSION = 1;

    // Table names
    private static final String USERS_TABLE = "users";
    private static final String USER_PROFILES_TABLE = "user_profiles";
    private static final String MEALS_TABLE = "meals";
    private static final String ORDERS_TABLE = "orders";
    private static final String PAYMENTS_TABLE = "payments";

    // Users table columns
    private static final String USER_ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String ADDRESS = "address";
    private static final String CREATED_AT = "created_at";

    // User profiles table columns
    private static final String PROFILE_ID = "id";
    private static final String PROFILE_USER_ID = "user_id";

    // Meals table columns
    private static final String MEAL_ID = "id";
    private static final String MEAL_NAME = "name";
    private static final String MEAL_DESCRIPTION = "description";
    private static final String MEAL_PRICE = "price";
    private static final String MEAL_CATEGORY = "category";

    // Orders table columns
    private static final String ORDER_ID = "id";
    private static final String ORDER_USER_ID = "user_id";
    private static final String ORDER_MEAL_ID = "meal_id";
    private static final String ORDER_QUANTITY = "quantity";
    private static final String ORDER_STATUS = "status";
    private static final String ORDER_DATE = "order_date";

    // Payments table columns
    private static final String PAYMENT_ID = "id";
    private static final String PAYMENT_ORDER_ID = "order_id";
    private static final String PAYMENT_AMOUNT = "amount";
    private static final String PAYMENT_METHOD = "method";
    private static final String PAYMENT_DATE = "payment_date";

    // Constructor
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + USERS_TABLE + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME + " TEXT UNIQUE NOT NULL, "
                + PASSWORD + " TEXT NOT NULL, "
                + EMAIL + " TEXT UNIQUE NOT NULL, "
                + PHONE + " TEXT, "
                + CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + ADDRESS + " TEXT )";

        // Create user profiles table
        String createUserProfilesTable = "CREATE TABLE " + USER_PROFILES_TABLE + " ("
                + PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PROFILE_USER_ID + " INTEGER, "
                + "FOREIGN KEY (" + PROFILE_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE)";

        // Create meals table
        String createMealsTable = "CREATE TABLE " + MEALS_TABLE + " ("
                + MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MEAL_NAME + " TEXT UNIQUE NOT NULL, "
                + MEAL_DESCRIPTION + " TEXT, "
                + MEAL_PRICE + " DECIMAL(10, 2), "
                + MEAL_CATEGORY + " TEXT)";

        // Create orders table
        String createOrdersTable = "CREATE TABLE " + ORDERS_TABLE + " ("
                + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ORDER_USER_ID + " INTEGER, "
                + ORDER_MEAL_ID + " INTEGER, "
                + ORDER_QUANTITY + " INTEGER, "
                + ORDER_STATUS + " TEXT, "
                + ORDER_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (" + ORDER_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY (" + ORDER_MEAL_ID + ") REFERENCES " + MEALS_TABLE + "(" + MEAL_ID + ") ON DELETE CASCADE)";

        // Create payments table
        String createPaymentsTable = "CREATE TABLE " + PAYMENTS_TABLE + " ("
                + PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PAYMENT_ORDER_ID + " INTEGER, "
                + PAYMENT_AMOUNT + " DECIMAL(10, 2), "
                + PAYMENT_METHOD + " TEXT, "
                + PAYMENT_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (" + PAYMENT_ORDER_ID + ") REFERENCES " + ORDERS_TABLE + "(" + ORDER_ID + ") ON DELETE CASCADE)";

        // Execute the SQL statements to create tables
        db.execSQL(createUsersTable);
        db.execSQL(createUserProfilesTable);
        db.execSQL(createMealsTable);
        db.execSQL(createOrdersTable);
        db.execSQL(createPaymentsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_PROFILES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEALS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENTS_TABLE);
        onCreate(db);
    }

    // Método para verificar o login de um usuário
    public boolean validateUserLogin(String emailOrPhone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE (email = ?) AND password = ?",
                new String[]{emailOrPhone, password});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public void addNewUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Adiciona os valores dos campos
        values.put("USERNAME", name);
        values.put("EMAIL", email);
        values.put("PASSWORD", password); // Em um app real, use hashing para a senha!

        // Insere o novo utilizador na tabela
        db.insert("users", null, values);
        db.close();
    }

    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("username");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            int moradaIndex = cursor.getColumnIndex("address");
            int tlmIndex = cursor.getColumnIndex("phone");

            // Verifica se as colunas existem antes de aceder
            if (nameIndex == -1 || emailIndex == -1 || passwordIndex == -1) {
                cursor.close();
                db.close();
                throw new IllegalArgumentException("Column not found in database");
            }

            String nome = cursor.getString(nameIndex);
            String emailUsuario = cursor.getString(emailIndex);
            String password = cursor.getString(passwordIndex);
            String morada = cursor.getString(moradaIndex);
            String phone = cursor.getString(tlmIndex);

            User user = new User(nome, emailUsuario, morada, phone);
            cursor.close();
            db.close();
            return user;
        }

        cursor.close();
        db.close();
        return null;
    }

    public User getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        int nameIndex = cursor.getColumnIndex("username");
        int emailIndex = cursor.getColumnIndex("email");
        int moradaIndex = cursor.getColumnIndex("address");
        int tlmIndex = cursor.getColumnIndex("phone");

        if (cursor != null && cursor.moveToFirst()) {
            String nome = cursor.getString(nameIndex);
            String emailUsuario = cursor.getString(emailIndex);
            String morada = cursor.getString(moradaIndex);
            String phone = cursor.getString(tlmIndex);

            User user = new User(nome, emailUsuario, morada, phone);
            cursor.close();

            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }



    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isRegistered;
    }

    public void editProfile(String name, String email, String address, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("USERNAME", name);
        values.put("EMAIL", email);
        values.put("ADDRESS", address);
        values.put("PHONE", phone);
        db.update("users", values, "EMAIL = ?", new String[]{email});
        db.close();
    }




}
