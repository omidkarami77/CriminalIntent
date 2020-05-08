package com.example.criminalintent.model.database;

public class CrimeDBSchema {
    public static final String NAME = "crime.db";

    public static final class Crime {
        public static final String NAME = "Crime";

        public static final class Cols {
            public static final String _ID = "_id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";

        }
    }

}
