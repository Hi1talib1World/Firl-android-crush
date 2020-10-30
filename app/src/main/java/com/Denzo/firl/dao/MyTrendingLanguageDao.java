package com.Denzo.firl.dao;

import android.database.sqlite.SQLiteStatement;

import androidx.room.Database;

public class MyTrendingLanguageDao extends AbstractDao<MyTrendingLanguage, String> {

    public static final String TABLENAME = "MY_TRENDING_LANGUAGE";

    /**
     * Properties of entity MyTrendingLanguage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Slug = new Property(0, String.class, "slug", true, "SLUG");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Order = new Property(2, int.class, "order", false, "ORDER");
    }


    public MyTrendingLanguageDao(DaoConfig config) {
        super(config);
    }

    public MyTrendingLanguageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MY_TRENDING_LANGUAGE\" (" + //
                "\"SLUG\" TEXT PRIMARY KEY NOT NULL ," + // 0: slug
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"ORDER\" INTEGER NOT NULL );"); // 2: order
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MY_TRENDING_LANGUAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MyTrendingLanguage entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getSlug());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getOrder());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MyTrendingLanguage entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getSlug());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getOrder());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }

    @Override
    public MyTrendingLanguage readEntity(Cursor cursor, int offset) {
        MyTrendingLanguage entity = new MyTrendingLanguage( //
                cursor.getString(offset + 0), // slug
                cursor.getString(offset + 1), // name
                cursor.getInt(offset + 2) // order
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, MyTrendingLanguage entity, int offset) {
        entity.setSlug(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setOrder(cursor.getInt(offset + 2));
    }

    @Override
    protected final String updateKeyAfterInsert(MyTrendingLanguage entity, long rowId) {
        return entity.getSlug();
    }

    @Override
    public String getKey(MyTrendingLanguage entity) {
        if(entity != null) {
            return entity.getSlug();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MyTrendingLanguage entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}