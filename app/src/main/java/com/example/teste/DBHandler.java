package com.example.teste;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    public static class User {
        private String name;
        private String email;
        private String password;
        private String morada;
        private String phone;
        private int id;

        // Construtor
        public User(String name, String email, String morada, String phone){
            this.name = name;
            this.email = email;
            this.morada = morada;
            this.phone = phone;
            this.id = id;

        }

        // Getters e Setters
        public String getName() {
            return name;
        }

        public int getId() {
            return id;
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

    public static class Meal {
        private String name;
        private String description;
        private double price;
        private int image;
        private int id;

        public Meal(String name, String description, double price, int image, int id) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.image = image;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }

        public int getImage() {
            return image;
        }

        public void setName() {
            this.name = name;
        }

        public void setDescription() {
            this.description = description;
        }

        public void setPrice() {
            this.price = price;
        }

        public void setImage() {
            this.image = image;
        }
    }

    public class CartItem {
        private int id;
        private Meal meal;
        private int quantity;

        public CartItem(int id, Meal meal, int quantity) {
            this.id = id;
            this.meal = meal;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public Meal getMeal() {
            return meal;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalPrice() {
            return meal.getPrice() * quantity;
        }
    }

    public static class Order {
        private int id;
        private Meal meal;
        private int quantity;
        private String status;
        private String date;

        public Order(int id, Meal meal, int quantity, String status, String date) {
            this.id = id;
            this.meal = meal;
            this.quantity = quantity;
            this.status = status;
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public Meal getMeal() {
            return meal;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getStatus() {
            return status;
        }

        public String getDate() {
            return date;
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
    private static final String CART_TABLE = "cart";

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
    private static final String MEAL_IMAGE = "image";
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

    // Cart table columns
    private static final String CART_ID = "id";
    private static final String CART_USER_ID = "user_id";
    private static final String CART_MEAL_ID = "meal_id";
    private static final String CART_QUANTITY = "quantity";
    private static final String CART_STATUS = "status";
    private static final String CART_DATE = "cart_date";

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
                + MEAL_IMAGE + " BLOB, "
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


        String createCartTable = "CREATE TABLE " + CART_TABLE + " ("
                + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CART_USER_ID + " INTEGER, "
                + CART_MEAL_ID + " INTEGER, "
                + CART_QUANTITY + " INTEGER, "
                + CART_STATUS + " TEXT, "
                + CART_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (" + CART_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY (" + CART_MEAL_ID + ") REFERENCES " + MEALS_TABLE + "(" + MEAL_ID + ") ON DELETE CASCADE)";
        // Execute the SQL statements to create tables
        db.execSQL(createUsersTable);
        db.execSQL(createUserProfilesTable);
        db.execSQL(createMealsTable);
        db.execSQL(createOrdersTable);
        db.execSQL(createPaymentsTable);
        db.execSQL(createCartTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_PROFILES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEALS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

            if (cursor.moveToFirst()) {
                String nome = cursor.getString(cursor.getColumnIndex("username"));
                String emailUsuario = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String morada = cursor.getString(cursor.getColumnIndex("address"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));

                return new User(nome, emailUsuario, morada, phone);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // NÃO fechar o db aqui, pois pode ser usado em outros métodos
        }
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

    @SuppressLint("Recycle")
    public void addMeal() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verifica se a tabela já contém registos
        if (db.query(MEALS_TABLE, null, null, null, null, null, null).getCount() <= 0) {
            // Criação dos primeiros registos

            ContentValues values = new ContentValues();
            values.put(MEAL_NAME, "Sushi");
            values.put(MEAL_DESCRIPTION, "Boiled sushi, or maki, is sushi where ingredients like fish, vegetables, and rice are wrapped in dried seaweed sheets and then sliced into bite-sized pieces.");
            values.put(MEAL_IMAGE, R.drawable.sushi_image);
            values.put(MEAL_PRICE, 9.99);
            values.put(MEAL_CATEGORY, "Main Course");
            db.insert(MEALS_TABLE, null, values);

            ContentValues values1 = new ContentValues();
            values1.put(MEAL_NAME, "Salada com Lombo");
            values1.put(MEAL_DESCRIPTION, "A healthy salad with tender loin pieces, vegetables, and a light dressing.");
            values1.put(MEAL_IMAGE, R.drawable.salad_image);
            values1.put(MEAL_PRICE, 8.99);
            values1.put(MEAL_CATEGORY, "Main Course");
            db.insert(MEALS_TABLE, null, values1);

            ContentValues values2 = new ContentValues();
            values2.put(MEAL_NAME, "Frango com Arroz");
            values2.put(MEAL_DESCRIPTION, "Tender chicken served with perfectly cooked rice and a side of vegetables.");
            values2.put(MEAL_IMAGE, R.drawable.chicken_rice_image);
            values2.put(MEAL_PRICE, 9.99);
            values2.put(MEAL_CATEGORY, "Main Course");
            db.insert(MEALS_TABLE, null, values2);
        }

        db.close();
    }


    public List<Meal> getMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM meals", null);

        List<Meal> meals = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Meal meal = new Meal(
                        cursor.getString(cursor.getColumnIndexOrThrow(MEAL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MEAL_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(MEAL_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_ID))
                );
                meals.add(meal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return meals;
    }

    @SuppressLint("Recycle")
    public void addAdmin() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(db.query("users", null, "email = ?", new String[]{"admin"}, null, null, null).getCount() > 0){
            db.close();
            return;
        }
        else{
            values.put(USERNAME, "admin");
            values.put(EMAIL, "admin");
            values.put(PASSWORD, "admin");

            db.insert("users", null, values);
            db.close();
        }

    }

    @SuppressLint("Range")
    public void addCart(Meal meal, int iduser, int idmeal) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verifica se o item já existe no carrinho
        String query = "SELECT * FROM " + CART_TABLE +
                " WHERE " + CART_USER_ID + " = ? AND " + CART_MEAL_ID + " = ? AND " + CART_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(iduser), String.valueOf(idmeal), "pending" });

        if (cursor.moveToFirst()) {
            // Se a refeição já existe no carrinho, atualiza a quantidade
            int currentQuantity = cursor.getInt(cursor.getColumnIndex(CART_QUANTITY));
            int newQuantity = currentQuantity + 1;

            ContentValues values = new ContentValues();
            values.put(CART_QUANTITY, newQuantity);

            // Atualiza a quantidade na base de dados
            db.update(CART_TABLE, values,
                    CART_USER_ID + " = ? AND " + CART_MEAL_ID + " = ? AND " + CART_STATUS + " = ?",
                    new String[] { String.valueOf(iduser), String.valueOf(idmeal), "pending" });
        } else {
            // Caso não exista, insere uma nova refeição no carrinho com quantidade 1
            ContentValues values = new ContentValues();
            values.put(CART_USER_ID, iduser);
            values.put(CART_MEAL_ID, idmeal);
            values.put(CART_QUANTITY, 1);
            values.put(CART_STATUS, "pending");

            db.insert(CART_TABLE, null, values);
        }

        cursor.close();
        db.close();
    }

    @SuppressLint("Range")
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ?",
                new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();
        return userId;
    }

    public Meal getMealById(int mealId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM meals WHERE id = ?", new String[]{String.valueOf(mealId)});

        if (cursor.moveToFirst()) {
            Meal meal = new Meal(
                    cursor.getString(cursor.getColumnIndexOrThrow(MEAL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MEAL_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(MEAL_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_IMAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_ID))
            );
            cursor.close();
            // Do not close the database here, as it may still be needed by the caller.
            return meal;
        }

        cursor.close();
        // Do not close the database here, as it may still be needed by the caller.
        return null;
    }

    @SuppressLint("Range")
    public List<CartItem> getCartItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<CartItem> cartItems = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM cart WHERE user_id = ? AND status = ?",
                    new String[]{String.valueOf(userId), "pending"});

            if (cursor.moveToFirst()) {
                do {
                    int cartItemId = cursor.getInt(cursor.getColumnIndexOrThrow(CART_ID));
                    int mealId = cursor.getInt(cursor.getColumnIndexOrThrow(CART_MEAL_ID));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(CART_QUANTITY));

                    Meal meal = getMealById(mealId);
                    if (meal != null) {
                        CartItem cartItem = new CartItem(cartItemId, meal, quantity);
                        cartItems.add(cartItem);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // Do not close the database here, as it may still be needed by the caller.
        }
        return cartItems;
    }

    @SuppressLint("Range")
    public double getTotalCart(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        double total = 0.0;

        try {
            String query = "SELECT SUM(c." + CART_QUANTITY + " * m." + MEAL_PRICE + ") AS total " +
                    "FROM " + CART_TABLE + " c " +
                    "JOIN " + MEALS_TABLE + " m ON c." + CART_MEAL_ID + " = m." + MEAL_ID +
                    " WHERE c." + CART_USER_ID + " = ? AND c." + CART_STATUS + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId), "pending"});

            if (cursor.moveToFirst()) {
                total = cursor.getDouble(cursor.getColumnIndex("total"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // NÃO fechar o db aqui, pois pode ser usado em outros métodos
        }

        return total;
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, CART_USER_ID + " = ? AND " + CART_STATUS + " = ?",
                new String[] { String.valueOf(userId), "pending" });
        db.close();
    }

    public void finishOrder(int userId, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            List<CartItem> cartItems = getCartItems(userId);

            for (CartItem cartItem : cartItems) {
                ContentValues orderValues = new ContentValues();
                orderValues.put(ORDER_USER_ID, userId);
                orderValues.put(ORDER_MEAL_ID, cartItem.getMeal().getId());
                orderValues.put(ORDER_QUANTITY, cartItem.getQuantity());
                orderValues.put(ORDER_STATUS, "ordered");

                long orderId = db.insert(ORDERS_TABLE, null, orderValues);

                ContentValues paymentValues = new ContentValues();
                paymentValues.put(PAYMENT_ORDER_ID, orderId);
                paymentValues.put(PAYMENT_AMOUNT, cartItem.getTotalPrice());
                paymentValues.put(PAYMENT_METHOD, paymentMethod);
                paymentValues.put(PAYMENT_DATE, "CURRENT_TIMESTAMP");
                db.insert(PAYMENTS_TABLE, null, paymentValues);
            }

            ContentValues cartValues = new ContentValues();
            cartValues.put(CART_STATUS, "ordered");
            db.update(CART_TABLE, cartValues, CART_USER_ID + " = ? AND " + CART_STATUS + " = ?",
                    new String[]{String.valueOf(userId), "pending"});
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    public List<Order> getOrdersByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> orders = new ArrayList<>();

        // Query para buscar as encomendas do usuário
        String query = "SELECT * FROM " + ORDERS_TABLE + " WHERE " + ORDER_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndex(ORDER_ID));
                int mealId = cursor.getInt(cursor.getColumnIndex(ORDER_MEAL_ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(ORDER_QUANTITY));
                String status = cursor.getString(cursor.getColumnIndex(ORDER_STATUS));
                String orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));

                Meal meal = getMealById(mealId);

                Order order = new Order(orderId, meal, quantity, status, orderDate);
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }



}
