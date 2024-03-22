package edu.bethlehem.scinexus.ResearchPaper;

public enum ResearchLanguage {

        ENGLISH,
        CHINESE,
        SPANISH,
        GERMAN,
        FRENCH,
        JAPANESE,
        PORTUGUESE,
        RUSSIAN,
        ITALIAN,
        KOREAN;


        public static ResearchLanguage[] getAllLanguages() {
            return ResearchLanguage.values();
        }
    }

