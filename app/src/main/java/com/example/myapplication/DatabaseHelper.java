package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.classes.BookMainItem;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "audiobooks.db";
    private static final int SCHEMA = 30; // версия базы данных

    // названия столбцов
    public static final String TABLE_BI = "BookItem"; // таблица личных книг
    public static final String TABLE_BI_REC = "BookRecommendItem"; // таблица личных книг

    public static final String COLUMN_BOOK_ID = "BookId";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_AUTHOR = "Author";
    public static final String COLUMN_READER = "Reader";
    public static final String COLUMN_PATH = "Path";
    public static final String COLUMN_DESC = "Description";
    public static final String COLUMN_TIME = "TimeCode";
    public static final String COLUMN_DATE_START = "DateStart";
    public static final String COLUMN_GENRE = "Genre";


    public static final String TABLE_NOTETYPES = "Note"; // типы заметок
    public static final String COLUMN_NOTETYPES_ID = "NoteTypeId";
    public static final String COLUMN_NOTETYPES_NAME = "NoteTypeName";


    public static final String TABLE_NOTES = "BookNotes"; // заметки по книге
    public static final String COLUMN_NOTES_TITLE = "NoteTitle";
    public static final String COLUMN_NOTES_DESC = "NoteDesc";
    public static final String COLUMN_NOTES_TIMECODE = "NoteTimecode";

    public static final String TABLE_SETTINGS = "Settings"; // настройки
    public static final String COLUMN_SETTINGS_ID = "SettingsId";
    public static final String COLUMN_SETTINGS_BAND1 = "SettingsBand1";
    public static final String COLUMN_SETTINGS_BAND2 = "SettingsBand2";
    public static final String COLUMN_SETTINGS_BAND3 = "SettingsBand3";
    public static final String COLUMN_SETTINGS_BAND4 = "SettingsBand4";
    public static final String COLUMN_SETTINGS_BAND5 = "SettingsBand5";



    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE "+ TABLE_BI +"(" + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                  + COLUMN_TITLE + " TEXT, "
                                                  + COLUMN_AUTHOR + " TEXT, "
                                                  + COLUMN_READER + " TEXT, "
                                                  + COLUMN_PATH + " TEXT, "
                                                  + COLUMN_DESC + " TEXT, "
                                                  + COLUMN_TIME + " INTEGER, "
                                                  + COLUMN_GENRE + " TEXT, "
                                                  + COLUMN_DATE_START + " TEXT);");


        db.execSQL("CREATE TABLE "+ TABLE_BI_REC +"(" + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_PATH + " TEXT, "
                + COLUMN_DESC + " TEXT, "
                + COLUMN_GENRE + " TEXT);");

        db.execSQL("CREATE TABLE "+ TABLE_SETTINGS +"(" + COLUMN_SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SETTINGS_BAND1 + " INTEGER, "
                + COLUMN_SETTINGS_BAND2 + " INTEGER, "
                + COLUMN_SETTINGS_BAND3 + " INTEGER, "
                + COLUMN_SETTINGS_BAND4 + " INTEGER, "
                + COLUMN_SETTINGS_BAND5 + " INTEGER);");

        db.execSQL("INSERT INTO "+TABLE_SETTINGS+ " VALUES(0, 1800, 1500, 1500, 1500, 1800), (1, 1800, 1500, 1500, 1500, 1800);");

        db.execSQL("INSERT INTO "+TABLE_BI+ " VALUES(0, '1984', 'Джордж Оруэлл', 'Руслан Драпалюк', 'path/', 'Одна из самых знаменитых антиутопий XX века – роман «1984» английского писателя Джорджа Оруэлла (1903–1950) был написан в 1948 году и продолжает тему «преданной революции», раскрытую в «Скотном дворе». По Оруэллу, нет и не может быть ничего ужаснее тотальной несвободы. Тоталитаризм уничтожает в человеке все духовные потребности, мысли, чувства и сам разум, оставляя лишь постоянный страх и единственный выбор – между молчанием и смертью, и если Старший Брат смотрит на тебя и заявляет, что «дважды два – пять», значит, так и есть.', 20000, 'Антиутопия', '2023-03-25 13:01:01'), " +
                                                   "(1, 'Рассказ неизвестного человека', 'Чехов Антон', 'Бухмин Аркадий', 'path/', 'Повествование ведется от лица «неизвестного человека», который отправился в Санкт-Петербург для внедрения в окружение врага. Маскируясь под слугу, он поступил на службу в богатую семью. В конечном итоге герой разочаровался в секретной миссии и своей жизни в целом.', 20000, 'Проза', '2023-03-31 16:45:01'), " +
                                                   "(2, 'Окраина', 'Тихонов Александр', 'Шубин Олег', 'path/', 'В сборнике «Окраина» писателя Александра Тихонова всего пять рассказов, по количеству этажей в обычной советской «панельке», где на каждом этаже, за каждым окном своя жизнь. Герои рассказов оказались на окраине: одни на окраине города, другие на окраине прежней жизни. Кому-то из них предстоит столкнуться с мистическим домом, высасывающим жизненные соки из жителей окраинного района, другим — взглянуть в лицо собственным страхам, а из третьих будут вытягивать жизнь неудачные отношения и жестокие люди. Неужто, окраина определяет, кем мы станем?.', 20000, 'Фантастика', '2023-04-05 12:45:01'), " +
                                                   "(3, '75000', 'Чехов Антон', 'Абдуллаев Джахангир', 'path/', 'Может, братья Луи и Огюст Люмьеры читали рассказы Чехова, и они на них так сильно повлияли, что изобрели свой «Cinématographe»?', 20000, 'Сатира', '2023-04-12 18:45:01'), " +
                                                   "(4, 'Каркассонн', 'Фолкнер Уильям', 'Воробьев Александр', 'path/', 'А я верхом на кауром коньке, у которого глаза — как синие электрические вспышки, а грива — как мятущееся пламя, и он мчится галопом вверх по холму и дальше прямо в высокое небо мира.', 20000, 'История', '2023-04-16 19:45:01'), " +
                                                   "(5, 'Три-четыре', 'Азимов Айзек', 'Марк Дейвс', 'path/', 'Айсидор Уэлби только что демобилизовался из армии и обнаружил, что в активе у него лишь хромота, а в пассиве — прощальное письмо от девушки, которую он всё ещё любил. И вот тут-то к нему и явился демон по имени Шапур. И предложил сделку. Как это водится у демонов — с подвохом', 20000, 'Мистика', '2023-05-01 19:45:01'), " +
                                                   "(6, 'Коты-воители: Стань диким!', 'Эрин Хантер', 'Анна Стрелец', 'path/', 'Стань диким - рассказывает об удивительной и полной приключений жизни домашнего котенка Рыжика, впервые попавшего в лес, где воют между собой четыре клана диких котов. Ему приходится доказывать, что он достоин чести стать воином и принадлежать к Грозному племени. Скоро лес становится настоящим домом для Рыжика: он храбро сражается за свое племя, находит настоящих друзей и наживает опасных врагов', 27960590, 'Фантастика', '2023-05-05 19:45:01');");

        // добавление начальных данных
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BI);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BI_REC);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SETTINGS);
        onCreate(db);
    }
}
