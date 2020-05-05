package com.example.bladesaber.tvappvc1.SqlManager;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class TextSetting {

    private TextDataItem data;

    private class TextDataItem {
        private int id;
        private int is_hidden;
        private String color;
        private float font_size;
        private String position = "bottom";
        private float speed;

        public int getIs_hidden() {
            return is_hidden;
        }

        public String getColor() {
            return color;
        }

        public float getFont_size() {
            return font_size;
        }

        public String getPosition() {
            return position;
        }

        public float getSpeed() {
            return speed;
        }

        public int getId() {
            return id;
        }
    }

    public int getIs_hidden() {
        return data.getIs_hidden();
    }

    public String getColor() {
        return data.getColor();
    }

    public float getFont_size() {
        return data.getFont_size();
    }

    public String getPosition() {
        return data.getPosition();
    }

    public float getSpeed() {
        return data.getSpeed();
    }

    public int getId() {
        return data.getId();
    }

}
