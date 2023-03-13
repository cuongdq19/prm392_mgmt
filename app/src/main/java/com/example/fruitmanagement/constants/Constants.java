package com.example.fruitmanagement.constants;

public final class Constants {
    public static final String SHARED_PREFERENCE_NAME = "com.example.fruitmanagement";

    public static final String DB_NAME = "management.db";

    public static final String CREATE_USER_QUERY = "CREATE TABLE \"user\" (\n" +
            "\t\"username\"\tTEXT NOT NULL,\n" +
            "\t\"password\"\tTEXT,\n" +
            "\t\"role\"\tTEXT,\n" +
            "\t\"email\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"username\")\n" +
            ")";

    public static final String CREATE_FRUIT_QUERY = "CREATE TABLE IF NOT EXISTS \"fruit\" (\n" +
            "\t\"id\"\tINTEGER NOT NULL,\n" +
            "\t\"name\"\tTEXT,\n" +
            "\t\"description\"\tINTEGER,\n" +
            "\t\"price\"\tNUMERIC,\n" +
            "\t\"quantity\"\tINTEGER,\n" +
            "\t\"image\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"id\")\n" +
            ")";

    public static final String CREATE_CART_QUERY = "CREATE TABLE \"cart\" (\n" +
            "\t\"fruit_id\"\tINTEGER NOT NULL,\n" +
            "\t\"price\"\tNUMERIC NOT NULL,\n" +
            "\t\"quantity\"\tINTEGER NOT NULL,\n" +
            "\t\"fruit_name\"\tTEXT NOT NULL,\n" +
            "\t\"image\"\tTEXT NOT NULL,\n" +
            "\tPRIMARY KEY(\"fruit_id\")\n" +
            ")";

    public static final String CREATE_ORDER_QUERY = "CREATE TABLE \"order\" (\n" +
            "\t\"id\"\tINTEGER PRIMARY KEY NOT NULL,\n" +
            "\t\"user_id\"\tTEXT NOT NULL,\n" +
            "\t\"created_time\"\tdate NOT NULL\n" +
            //"\tPRIMARY KEY(\"id\")\n" +
            ")";

    public static final String CREATE_ORDERDETAIL_QUERY = "CREATE TABLE \"order_detail\" (\n" +
            "\t\"id\"\tINTEGER PRIMARY KEY NOT NULL,\n" +
            "\t\"order_id\"\tINTEGER NOT NULL,\n" +
            "\t\"fruit_id\"\tINTEGER NOT NULL,\n" +
            "\t\"quantity\"\tINTEGER NOT NULL\n" +
            //"\tPRIMARY KEY(\"id\")\n" +
            ")";

    public static final String DROP_USER_QUERY = "DROP TABLE IF EXISTS User";
    public static final String DROP_FRUIT_QUERY = "DROP TABLE IF EXISTS Fruit";
    public static final String DROP_CART_QUERY = "DROP TABLE IF EXISTS Cart";
    public static final String DROP_ORDER_QUERY = "DROP TABLE IF EXISTS Order";
    public static final String DROP_ORDERDETAIL_QUERY = "DROP TABLE IF EXISTS order_detail";

    public static final String SEED_FRUIT_QUERY = "INSERT INTO \"fruit\" (\"id\",\"name\",\"description\",\"price\",\"quantity\",\"image\") VALUES " +
            "(1,'Banana','Banana',10000,100,'https://target.scene7.com/is/image/Target/GUEST_f5d0cfc3-9d02-4ee0-a6c6-ed5dc09971d1?wid=488&hei=488&fmt=pjpeg')," +
            "(2,'Apple','Apple',30000,100,'https://dictionary.cambridge.org/vi/images/thumb/apple_noun_001_00650.jpg?version=5.0.295')," +
            "(3,'Strawberry','Strawberry',15000,100,'https://www.thermofisher.com/blog/food/wp-content/uploads/sites/5/2015/08/single_strawberry__isolated_on_a_white_background.jpg')," +
            "(4,'Carrot','Carrot',9000,5,'https://bizweb.dktcdn.net/100/433/358/products/bc5480dd-2263-4ce9-a94a-37bd202566fa.jpg?v=1654351336440')";

    public static final String SEED_USER_QUERY = "INSERT INTO \"user\" (\"username\",\"password\",\"role\",\"email\") VALUES " +
            "('admin','1','Admin','admin@gmail.com'),"+
            "('user','1','User','user@gmail.com');\n";
}
